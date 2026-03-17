package com.app.backup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.workers.BackupWorker;
import com.app.backup.workers.BootReceiver;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class BackupViewModel extends ViewModel {

    private final BackupManager backupManager;
    private final BackupRepository backupRepository;
    private final DriveService driveService;
    private final Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Boolean> isBackingUp = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> backupProgress = new MutableLiveData<>(0);
    private final MutableLiveData<String> backupStage = new MutableLiveData<>();
    private final MutableLiveData<BackupEvent> backupEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUploadingToDrive = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> uploadProgress = new MutableLiveData<>(0);

    @Inject
    public BackupViewModel(@ApplicationContext Context context,
                            BackupManager backupManager,
                            BackupRepository backupRepository,
                            DriveService driveService) {
        this.context = context;
        this.backupManager = backupManager;
        this.backupRepository = backupRepository;
        this.driveService = driveService;
    }

    public LiveData<Boolean> isBackingUp() { return isBackingUp; }
    public LiveData<Integer> getBackupProgress() { return backupProgress; }
    public LiveData<String> getBackupStage() { return backupStage; }
    public LiveData<BackupEvent> getBackupEvent() { return backupEvent; }
    public LiveData<Boolean> isUploadingToDrive() { return isUploadingToDrive; }
    public LiveData<Integer> getUploadProgress() { return uploadProgress; }
    public LiveData<List<BackupRecord>> getAllBackups() { return backupRepository.getAllBackups(); }

    public void startImmediateBackup(BackupManager.BackupOptions options) {
        if (Boolean.TRUE.equals(isBackingUp.getValue())) return;
        isBackingUp.setValue(true);
        backupProgress.setValue(0);

        // Track the last result to get filePath and backupId after completion
        final String[] lastFilePath = {null};
        final String[] lastBackupId = {null};

        disposables.add(
            backupManager.createBackup(options)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    progress -> {
                        backupProgress.setValue(progress.progress);
                        backupStage.setValue(progress.stage);
                        // Capture the file path and backup ID from the final STAGE_DONE result
                        if (progress.filePath != null) {
                            lastFilePath[0] = progress.filePath;
                        }
                        if (progress.backupId != null) {
                            lastBackupId[0] = progress.backupId;
                        }
                    },
                    error -> {
                        isBackingUp.setValue(false);
                        backupEvent.setValue(new BackupEvent(BackupEvent.Type.BACKUP_FAILED, error.getMessage()));
                    },
                    () -> {
                        isBackingUp.setValue(false);
                        backupEvent.setValue(new BackupEvent(BackupEvent.Type.BACKUP_SUCCESS, null));

                        // Auto-upload to Drive if the option was enabled
                        if (options.uploadToDrive && lastFilePath[0] != null && lastBackupId[0] != null) {
                            uploadToDrive(lastBackupId[0], lastFilePath[0]);
                        }
                    }
                )
        );
    }

    public void scheduleBackupWithWorkManager(boolean includeContacts, boolean includeSms) {
        Data inputData = new Data.Builder()
                .putBoolean(BackupWorker.KEY_INCLUDE_CONTACTS, includeContacts)
                .putBoolean(BackupWorker.KEY_INCLUDE_SMS, includeSms)
                .putBoolean(BackupWorker.KEY_UPLOAD_DRIVE, driveService.isInitialized())
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackupWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                "manual_backup",
                ExistingWorkPolicy.REPLACE,
                workRequest);

        backupEvent.setValue(new BackupEvent(BackupEvent.Type.BACKUP_QUEUED, null));
    }

    public void setupAutoBackup(int intervalHours) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(
                BackupWorker.class, intervalHours, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(BootReceiver.WORK_TAG_SCHEDULED)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                BootReceiver.WORK_TAG_SCHEDULED,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWork);
    }

    public void cancelAutoBackup() {
        WorkManager.getInstance(context).cancelUniqueWork(BootReceiver.WORK_TAG_SCHEDULED);
    }

    public void uploadToDrive(String backupId, String filePath) {
        driveService.ensureInitialized();
        if (!driveService.isInitialized()) {
            backupEvent.setValue(new BackupEvent(BackupEvent.Type.DRIVE_NOT_CONNECTED, null));
            return;
        }
        isUploadingToDrive.setValue(true);
        uploadProgress.setValue(0);

        disposables.add(
            driveService.uploadBackup(new File(filePath), backupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    progress -> uploadProgress.setValue(progress.progress),
                    error -> {
                        isUploadingToDrive.setValue(false);
                        backupEvent.setValue(new BackupEvent(BackupEvent.Type.UPLOAD_FAILED, error.getMessage()));
                    },
                    () -> {
                        isUploadingToDrive.setValue(false);
                        backupEvent.setValue(new BackupEvent(BackupEvent.Type.UPLOAD_SUCCESS, null));
                    }
                )
        );
    }

    public void deleteBackup(BackupRecord record) {
        // Delete local file if exists
        if (record.getFilePath() != null) {
            new File(record.getFilePath()).delete();
        }
        backupRepository.deleteBackup(record);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static class BackupEvent {
        public enum Type {
            BACKUP_SUCCESS, BACKUP_FAILED, BACKUP_QUEUED,
            UPLOAD_SUCCESS, UPLOAD_FAILED, DRIVE_NOT_CONNECTED
        }

        public final Type type;
        public final String message;

        public BackupEvent(Type type, String message) {
            this.type = type;
            this.message = message;
        }
    }
}
