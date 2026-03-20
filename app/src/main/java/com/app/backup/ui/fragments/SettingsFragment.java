package com.app.backup.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.backup.R;
import com.app.backup.viewmodel.SettingsViewModel;
import com.app.backup.workers.BackupWorker;
import com.app.backup.workers.BootReceiver;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsViewModel viewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        setupPreferenceListeners();
    }

    private void setupPreferenceListeners() {
        Preference clearOld = findPreference("clear_old_backups");
        if (clearOld != null) {
            clearOld.setOnPreferenceClickListener(p -> {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Clean Up")
                        .setMessage("Old and failed backups will be removed. Do you want to continue?")
                        .setPositiveButton("Clean Up", (d, w) -> {
                            // Cleanup backups older than 7 days by default when clicked manually
                            viewModel.cleanupOldBackups(7);
                            Snackbar.make(requireView(), "Cleanup started", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null).show();
                return true;
            });
        }

        Preference privacyPolicy = findPreference("privacy_policy");
        if (privacyPolicy != null) {
            privacyPolicy.setOnPreferenceClickListener(p -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://your-privacy-policy-url.com")));
                } catch (Exception e) {
                    Snackbar.make(requireView(), "Cannot open privacy policy", Snackbar.LENGTH_SHORT).show();
                }
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
        try {
            int hours = Integer.parseInt(prefs.getString("backup_interval_hours", "24"));
            PeriodicWorkRequest req = new PeriodicWorkRequest.Builder(
                    BackupWorker.class, hours, TimeUnit.HOURS)
                    .addTag(BootReceiver.WORK_TAG_SCHEDULED).build();
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                    BootReceiver.WORK_TAG_SCHEDULED, ExistingPeriodicWorkPolicy.UPDATE, req);
        } catch (NumberFormatException e) {
            // Fallback for invalid input
        }
    }
}
