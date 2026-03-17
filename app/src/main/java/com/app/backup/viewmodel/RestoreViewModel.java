package com.app.backup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.restore.RestoreManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@HiltViewModel
public class RestoreViewModel extends ViewModel {

    private final RestoreManager restoreManager;
    private final BackupRepository backupRepository;
    private final DriveService driveService;
    private final Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<RestoreManager.BackupPreview> backupPreview = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingPreview = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isRestoring = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> restoreProgress = new MutableLiveData<>(0);
    private final MutableLiveData<String> restoreMessage = new MutableLiveData<>();
    private final MutableLiveData<RestoreEvent> restoreEvent = new MutableLiveData<>();
    private final MutableLiveData<List<DriveService.DriveBackupFile>> driveBackups = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingDriveBackups = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isDownloading = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> downloadProgress = new MutableLiveData<>(0);

    @Inject
    public RestoreViewModel(@ApplicationContext Context context,
                             RestoreManager restoreManager,
                             BackupRepository backupRepository,
                             DriveService driveService) {
        this.context = context;
        this.restoreManager = restoreManager;
        this.backupRepository = backupRepository;
        this.driveService = driveService;
    }

    public LiveData<RestoreManager.BackupPreview> getBackupPreview() { return backupPreview; }
    public LiveData<Boolean> isLoadingPreview() { return isLoadingPreview; }
    public LiveData<Boolean> isRestoring() { return isRestoring; }
    public LiveData<Integer> getRestoreProgress() { return restoreProgress; }
    public LiveData<String> getRestoreMessage() { return restoreMessage; }
    public LiveData<RestoreEvent> getRestoreEvent() { return restoreEvent; }
    public LiveData<List<DriveService.DriveBackupFile>> getDriveBackups() { return driveBackups; }
    public LiveData<Boolean> isLoadingDriveBackups() { return isLoadingDriveBackups; }
    public LiveData<Boolean> isDownloading() { return isDownloading; }
    public LiveData<Integer> getDownloadProgress() { return downloadProgress; }
    public LiveData<List<BackupRecord>> getLocalBackups() { return backupRepository.getBackupsByType(BackupRecord.TYPE_LOCAL); }

    public void loadPreview(File backupFile) {
        isLoadingPreview.setValue(true);
        disposables.add(
            restoreManager.previewBackup(backupFile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    preview -> {
                        backupPreview.setValue(preview);
                        isLoadingPreview.setValue(false);
                    },
                    error -> {
                        isLoadingPreview.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.PREVIEW_FAILED, error.getMessage()));
                    }
                )
        );
    }

    public void startRestore(File backupFile, RestoreManager.RestoreOptions options) {
        if (Boolean.TRUE.equals(isRestoring.getValue())) return;
        isRestoring.setValue(true);
        restoreProgress.setValue(0);

        disposables.add(
            restoreManager.restoreBackup(backupFile, options)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    progress -> {
                        restoreProgress.setValue(progress.progress);
                        restoreMessage.setValue(progress.message);
                    },
                    error -> {
                        isRestoring.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.RESTORE_FAILED, error.getMessage()));
                    },
                    () -> {
                        isRestoring.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.RESTORE_SUCCESS, null));
                    }
                )
        );
    }

    public void loadDriveBackups() {
        driveService.ensureInitialized();
        if (!driveService.isInitialized()) {
            restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.DRIVE_NOT_CONNECTED, null));
            return;
        }
        isLoadingDriveBackups.setValue(true);
        disposables.add(
            driveService.listBackups()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    files -> {
                        driveBackups.setValue(files);
                        isLoadingDriveBackups.setValue(false);
                    },
                    error -> {
                        isLoadingDriveBackups.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.DRIVE_ERROR, error.getMessage()));
                    }
                )
        );
    }

    public void downloadFromDrive(String fileId, File outputFile) {
        isDownloading.setValue(true);
        downloadProgress.setValue(0);
        disposables.add(
            driveService.downloadBackup(fileId, outputFile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    progress -> downloadProgress.setValue(progress.progress),
                    error -> {
                        isDownloading.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.DOWNLOAD_FAILED, error.getMessage()));
                    },
                    () -> {
                        isDownloading.setValue(false);
                        restoreEvent.setValue(new RestoreEvent(RestoreEvent.Type.DOWNLOAD_SUCCESS, outputFile.getAbsolutePath()));
                    }
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static class RestoreEvent {
        public enum Type {
            RESTORE_SUCCESS, RESTORE_FAILED, PREVIEW_FAILED,
            DRIVE_NOT_CONNECTED, DRIVE_ERROR, DOWNLOAD_SUCCESS, DOWNLOAD_FAILED
        }
        public final Type type;
        public final String message;
        public RestoreEvent(Type type, String message) { this.type = type; this.message = message; }
    }
}
