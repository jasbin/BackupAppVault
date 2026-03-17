package com.app.backup.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DashboardViewModel extends ViewModel {

    private final BackupRepository backupRepository;
    private final ContactsRepository contactsRepository;
    private final SmsRepository smsRepository;
    private final BackupManager backupManager;
    private final Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Integer> contactCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> smsCount = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isLoadingCounts = new MutableLiveData<>(false);
    private final MutableLiveData<BackupResult> backupResult = new MutableLiveData<>();
    private final MutableLiveData<Integer> backupProgress = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isBackingUp = new MutableLiveData<>(false);
    private final MutableLiveData<String> backupStage = new MutableLiveData<>();
    private final MutableLiveData<Long> totalStorageUsed = new MutableLiveData<>(0L);

    @Inject
    public DashboardViewModel(@ApplicationContext Context context,
                               BackupRepository backupRepository,
                               ContactsRepository contactsRepository,
                               SmsRepository smsRepository,
                               BackupManager backupManager) {
        this.context = context;
        this.backupRepository = backupRepository;
        this.contactsRepository = contactsRepository;
        this.smsRepository = smsRepository;
        this.backupManager = backupManager;
    }

    // Expose LiveData
    public LiveData<List<BackupRecord>> getAllBackups() { return backupRepository.getAllBackups(); }
    public LiveData<BackupRecord> getLatestBackup() { return backupRepository.getLatestBackup(); }
    public LiveData<Integer> getContactCount() { return contactCount; }
    public LiveData<Integer> getSmsCount() { return smsCount; }
    public LiveData<Boolean> isLoadingCounts() { return isLoadingCounts; }
    public LiveData<BackupResult> getBackupResult() { return backupResult; }
    public LiveData<Integer> getBackupProgress() { return backupProgress; }
    public LiveData<Boolean> isBackingUp() { return isBackingUp; }
    public LiveData<String> getBackupStage() { return backupStage; }
    public LiveData<Long> getTotalStorageUsed() { return totalStorageUsed; }

    public void loadCounts() {
        isLoadingCounts.setValue(true);
        disposables.add(
            io.reactivex.rxjava3.core.Observable.fromCallable(() -> {
                int contacts = contactsRepository.getContactCount();
                int sms = smsRepository.getSmsCount();
                return new int[]{contacts, sms};
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(counts -> {
                contactCount.setValue(counts[0]);
                smsCount.setValue(counts[1]);
                isLoadingCounts.setValue(false);
            }, err -> isLoadingCounts.setValue(false))
        );
    }

    public void loadStorageStats() {
        disposables.add(
            io.reactivex.rxjava3.core.Observable.fromCallable(() ->
                    backupRepository.getTotalBackupSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(totalStorageUsed::setValue, err -> {})
        );
    }

    public void startBackup(BackupManager.BackupOptions options) {
        if (Boolean.TRUE.equals(isBackingUp.getValue())) return;
        isBackingUp.setValue(true);
        backupProgress.setValue(0);

        disposables.add(
            backupManager.createBackup(options)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    progress -> {
                        backupProgress.setValue(progress.progress);
                        backupStage.setValue(progress.stage);
                    },
                    error -> {
                        isBackingUp.setValue(false);
                        backupResult.setValue(new BackupResult(false, error.getMessage(), null));
                    },
                    () -> {
                        isBackingUp.setValue(false);
                        backupResult.setValue(new BackupResult(true, null, null));
                        loadStorageStats();
                    }
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static class BackupResult {
        public final boolean success;
        public final String errorMessage;
        public final String filePath;

        public BackupResult(boolean success, String errorMessage, String filePath) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.filePath = filePath;
        }
    }
}
