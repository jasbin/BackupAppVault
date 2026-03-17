package com.app.backup.workers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    public static final String WORK_TAG_SCHEDULED = "scheduled_backup";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) {
            Log.d(TAG, "Boot/update received, re-scheduling backups");
            rescheduleBackupsIfNeeded(context);
        }
    }

    private void rescheduleBackupsIfNeeded(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoBackupEnabled = prefs.getBoolean("auto_backup_enabled", false);
        if (!autoBackupEnabled) return;

        int intervalHours = prefs.getInt("backup_interval_hours", 24);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                BackupWorker.class, intervalHours, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(WORK_TAG_SCHEDULED)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG_SCHEDULED,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest);
        Log.d(TAG, "Scheduled periodic backup every " + intervalHours + " hours");
    }
}
