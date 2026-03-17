package com.app.backup.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.app.backup.R;
import com.app.backup.databinding.ActivitySettingsBinding;
import com.app.backup.workers.BootReceiver;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key == null) return;
            switch (key) {
                case "dark_mode":
                    boolean darkMode = prefs.getBoolean("dark_mode", false);
                    AppCompatDelegate.setDefaultNightMode(darkMode
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case "auto_backup_enabled":
                case "backup_interval_hours":
                    updateAutoBackupSchedule(prefs);
                    break;
            }
        }

        private void updateAutoBackupSchedule(SharedPreferences prefs) {
            boolean enabled = prefs.getBoolean("auto_backup_enabled", false);
            if (!enabled) {
                WorkManager.getInstance(requireContext())
                        .cancelUniqueWork(BootReceiver.WORK_TAG_SCHEDULED);
                return;
            }
            int hours = Integer.parseInt(prefs.getString("backup_interval_hours", "24"));
            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                    com.app.backup.workers.BackupWorker.class, hours, TimeUnit.HOURS)
                    .addTag(BootReceiver.WORK_TAG_SCHEDULED)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                    BootReceiver.WORK_TAG_SCHEDULED,
                    ExistingPeriodicWorkPolicy.UPDATE, request);
        }
    }
}
