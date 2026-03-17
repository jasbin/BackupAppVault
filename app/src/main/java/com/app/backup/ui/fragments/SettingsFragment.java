package com.app.backup.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.backup.R;
import com.app.backup.workers.BackupWorker;
import com.app.backup.workers.BootReceiver;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setupPreferenceListeners();
    }

    private void setupPreferenceListeners() {
        Preference clearOld = findPreference("clear_old_backups");
        if (clearOld != null) {
            clearOld.setOnPreferenceClickListener(p -> {
                WorkManager.getInstance(requireContext()).cancelUniqueWork(BootReceiver.WORK_TAG_SCHEDULED);
                // Also handled by BootReceiver on reschedule
                if (getActivity() != null) {
                    new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Clean Up")
                            .setMessage("Old and failed backups will be removed.")
                            .setPositiveButton("Clean Up", (d, w) -> {
                                com.app.backup.data.database.AppDatabase.getInstance(requireContext())
                                        .backupDao()
                                        .deleteFailedBackups();
                            })
                            .setNegativeButton("Cancel", null).show();
                }
                return true;
            });
        }

        Preference privacyPolicy = findPreference("privacy_policy");
        if (privacyPolicy != null) {
            privacyPolicy.setOnPreferenceClickListener(p -> {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://your-privacy-policy-url.com")));
                return true;
            });
        }
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
                AppCompatDelegate.setDefaultNightMode(
                        prefs.getBoolean("dark_mode", false)
                                ? AppCompatDelegate.MODE_NIGHT_YES
                                : AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "auto_backup_enabled":
            case "backup_interval_hours":
                updateAutoBackup(prefs);
                break;
        }
    }

    private void updateAutoBackup(SharedPreferences prefs) {
        boolean enabled = prefs.getBoolean("auto_backup_enabled", false);
        if (!enabled) {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(BootReceiver.WORK_TAG_SCHEDULED);
            return;
        }
        int hours = Integer.parseInt(prefs.getString("backup_interval_hours", "24"));
        PeriodicWorkRequest req = new PeriodicWorkRequest.Builder(
                BackupWorker.class, hours, TimeUnit.HOURS)
                .addTag(BootReceiver.WORK_TAG_SCHEDULED).build();
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                BootReceiver.WORK_TAG_SCHEDULED, ExistingPeriodicWorkPolicy.UPDATE, req);
    }
}
