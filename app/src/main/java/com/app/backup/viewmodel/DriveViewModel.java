package com.app.backup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.backup.data.drive.DriveService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@HiltViewModel
public class DriveViewModel extends ViewModel {

    private final DriveService driveService;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Boolean> isDriveConnected = new MutableLiveData<>(false);
    private final MutableLiveData<List<DriveService.DriveBackupFile>> driveFiles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<DriveEvent> driveEvent = new MutableLiveData<>();
    private final MutableLiveData<String> connectedAccount = new MutableLiveData<>();

    @Inject
    public DriveViewModel(@ApplicationContext Context context, DriveService driveService) {
        this.driveService = driveService;
    }

    public LiveData<Boolean> isDriveConnected() { return isDriveConnected; }
    public LiveData<List<DriveService.DriveBackupFile>> getDriveFiles() { return driveFiles; }
    public LiveData<Boolean> isLoading() { return isLoading; }
    public LiveData<DriveEvent> getDriveEvent() { return driveEvent; }
    public LiveData<String> getConnectedAccount() { return connectedAccount; }

    public void connectDrive(String accountName) {
        driveService.initWithAccount(accountName);
        isDriveConnected.setValue(true);
        connectedAccount.setValue(accountName);
        driveEvent.setValue(new DriveEvent(DriveEvent.Type.CONNECTED, null));
        loadDriveFiles();
    }

    public void disconnectDrive() {
        driveService.signOut();
        isDriveConnected.setValue(false);
        connectedAccount.setValue(null);
        driveFiles.setValue(null);
        driveEvent.setValue(new DriveEvent(DriveEvent.Type.DISCONNECTED, null));
    }

    public void loadDriveFiles() {
        if (!driveService.isInitialized()) return;
        isLoading.setValue(true);
        disposables.add(
            driveService.listBackups()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    files -> {
                        driveFiles.setValue(files);
                        isLoading.setValue(false);
                    },
                    error -> {
                        isLoading.setValue(false);
                        driveEvent.setValue(new DriveEvent(DriveEvent.Type.ERROR, error.getMessage()));
                    }
                )
        );
    }

    public void deleteDriveFile(String fileId) {
        disposables.add(
            driveService.deleteBackup(fileId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        driveEvent.setValue(new DriveEvent(DriveEvent.Type.FILE_DELETED, null));
                        loadDriveFiles();
                    },
                    error -> driveEvent.setValue(new DriveEvent(DriveEvent.Type.ERROR, error.getMessage()))
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static class DriveEvent {
        public enum Type { CONNECTED, DISCONNECTED, FILE_DELETED, ERROR }
        public final Type type;
        public final String message;
        public DriveEvent(Type type, String message) { this.type = type; this.message = message; }
    }
}
