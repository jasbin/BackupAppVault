package com.app.backup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BackupApplication extends Application implements Configuration.Provider {

    public static final String CHANNEL_BACKUP_ID = "backup_progress";
    public static final String CHANNEL_REMINDER_ID = "backup_reminder";
    public static final String CHANNEL_RESTORE_ID = "restore_progress";

    @Inject
    HiltWorkerFactory workerFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);

            // Backup Progress Channel
            NotificationChannel backupChannel = new NotificationChannel(
                    CHANNEL_BACKUP_ID,
                    "Backup Progress",
                    NotificationManager.IMPORTANCE_LOW
            );
            backupChannel.setDescription("Shows backup progress and completion status");
            backupChannel.setShowBadge(false);
            manager.createNotificationChannel(backupChannel);

            // Reminder Channel
            NotificationChannel reminderChannel = new NotificationChannel(
                    CHANNEL_REMINDER_ID,
                    "Backup Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            reminderChannel.setDescription("Reminds you to back up your data");
            manager.createNotificationChannel(reminderChannel);

            // Restore Channel
            NotificationChannel restoreChannel = new NotificationChannel(
                    CHANNEL_RESTORE_ID,
                    "Restore Progress",
                    NotificationManager.IMPORTANCE_LOW
            );
            restoreChannel.setDescription("Shows restore progress and completion status");
            restoreChannel.setShowBadge(false);
            manager.createNotificationChannel(restoreChannel);
        }
    }
}
