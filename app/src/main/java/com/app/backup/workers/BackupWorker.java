package com.app.backup.workers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.backup.BackupApplication;
import com.app.backup.R;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.ui.activities.MainActivity;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class BackupWorker extends Worker {

    private static final String TAG = "BackupWorker";
    public static final String KEY_INCLUDE_CONTACTS = "include_contacts";
    public static final String KEY_INCLUDE_SMS = "include_sms";
    public static final String KEY_UPLOAD_DRIVE = "upload_drive";
    private static final int NOTIF_ID = 1001;

    private final BackupManager backupManager;

    @AssistedInject
    public BackupWorker(@Assisted @NonNull Context context,
                        @Assisted @NonNull WorkerParameters params,
                        BackupManager backupManager) {
        super(context, params);
        this.backupManager = backupManager;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            setForegroundAsync(createForegroundInfo("Starting backup..."));

            boolean includeContacts = getInputData().getBoolean(KEY_INCLUDE_CONTACTS, true);
            boolean includeSms = getInputData().getBoolean(KEY_INCLUDE_SMS, true);
            boolean uploadDrive = getInputData().getBoolean(KEY_UPLOAD_DRIVE, false);

            BackupManager.BackupOptions options = new BackupManager.BackupOptions();
            options.includeContacts = includeContacts;
            options.includeSms = includeSms;
            options.encrypt = true;
            options.uploadToDrive = uploadDrive;

            backupManager.createBackup(options)
                    .blockingSubscribe(
                            progress -> {
                                setForegroundAsync(createForegroundInfo(getStageMessage(progress.stage)));
                                Log.d(TAG, "Backup progress: " + progress.progress + "% - " + progress.stage);
                            },
                            error -> Log.e(TAG, "Backup error", error),
                            () -> Log.d(TAG, "Backup completed")
                    );

            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "BackupWorker failed", e);
            return isStopped() ? Result.failure() : Result.retry();
        }
    }

    private String getStageMessage(String stage) {
        switch (stage) {
            case BackupManager.BackupResult.STAGE_READING_CONTACTS: return "Reading contacts...";
            case BackupManager.BackupResult.STAGE_READING_SMS: return "Reading SMS messages...";
            case BackupManager.BackupResult.STAGE_PACKAGING: return "Packaging backup...";
            case BackupManager.BackupResult.STAGE_COMPRESSING: return "Compressing files...";
            case BackupManager.BackupResult.STAGE_FINALIZING: return "Finalizing backup...";
            case BackupManager.BackupResult.STAGE_DONE: return "Backup complete!";
            default: return "Backing up...";
        }
    }

    private ForegroundInfo createForegroundInfo(String message) {
        Context ctx = getApplicationContext();
        Intent intent = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(ctx, BackupApplication.CHANNEL_BACKUP_ID)
                .setContentTitle("BackupVault")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_backup)
                .setOngoing(true)
                .setContentIntent(pi)
                .build();

        return new ForegroundInfo(NOTIF_ID, notification);
    }
}
