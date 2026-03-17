package com.app.backup.data.repository;

import androidx.lifecycle.LiveData;

import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.entity.BackupRecord;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class BackupRepository {

    private final AppDatabase database;

    @Inject
    public BackupRepository(AppDatabase database) {
        this.database = database;
    }

    public LiveData<List<BackupRecord>> getAllBackups() {
        return database.backupDao().getAllBackupsLive();
    }

    public LiveData<BackupRecord> getLatestBackup() {
        return database.backupDao().getLatestCompletedBackupLive();
    }

    public LiveData<List<BackupRecord>> getBackupsByType(String type) {
        return database.backupDao().getBackupsByType(type);
    }

    public BackupRecord getLatestBackupSync() {
        return database.backupDao().getLatestCompletedBackup();
    }

    public void insertBackup(BackupRecord record) {
        Completable.fromAction(() -> database.backupDao().insertBackup(record))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void updateBackup(BackupRecord record) {
        Completable.fromAction(() -> database.backupDao().updateBackup(record))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteBackup(BackupRecord record) {
        Completable.fromAction(() -> database.backupDao().deleteBackup(record))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteBackupById(String id) {
        Completable.fromAction(() -> database.backupDao().deleteBackupById(id))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void deleteFailedBackups() {
        Completable.fromAction(() -> database.backupDao().deleteFailedBackups())
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public long getTotalBackupSize() {
        return database.backupDao().getTotalBackupSize();
    }

    public int getCompletedBackupCount() {
        return database.backupDao().getCompletedBackupCount();
    }
}
