package com.app.backup.viewmodel;

import androidx.lifecycle.ViewModel;

import com.app.backup.data.repository.BackupRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final BackupRepository backupRepository;

    @Inject
    public SettingsViewModel(BackupRepository backupRepository) {
        this.backupRepository = backupRepository;
    }

    public void cleanupOldBackups(int days) {
        long cutoffTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days);
        backupRepository.cleanupOldBackups(cutoffTime);
    }
}
