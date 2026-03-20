package com.app.backup.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.backup.data.database.entity.BackupRecord;

import java.util.List;

@Dao
public interface BackupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBackup(BackupRecord record);

    @Update
    void updateBackup(BackupRecord record);

    @Delete
    void deleteBackup(BackupRecord record);

    @Query("SELECT * FROM backup_records ORDER BY created_at DESC")
    LiveData<List<BackupRecord>> getAllBackupsLive();

    @Query("SELECT * FROM backup_records ORDER BY created_at DESC")
    List<BackupRecord> getAllBackups();

    @Query("SELECT * FROM backup_records WHERE backup_id = :id LIMIT 1")
    BackupRecord getBackupById(String id);

    @Query("SELECT * FROM backup_records WHERE backup_type = :type ORDER BY created_at DESC")
    LiveData<List<BackupRecord>> getBackupsByType(String type);

    @Query("SELECT * FROM backup_records WHERE status = 'completed' ORDER BY created_at DESC LIMIT 1")
    BackupRecord getLatestCompletedBackup();

    @Query("SELECT * FROM backup_records WHERE status = 'completed' ORDER BY created_at DESC LIMIT 1")
    LiveData<BackupRecord> getLatestCompletedBackupLive();

    @Query("SELECT COUNT(*) FROM backup_records WHERE status = 'completed'")
    int getCompletedBackupCount();

    @Query("SELECT SUM(file_size_bytes) FROM backup_records WHERE status = 'completed'")
    long getTotalBackupSize();

    @Query("DELETE FROM backup_records WHERE backup_id = :id")
    void deleteBackupById(String id);

    @Query("DELETE FROM backup_records WHERE status = 'failed'")
    void deleteFailedBackups();

    @Query("SELECT * FROM backup_records WHERE created_at < :cutoffTime")
    List<BackupRecord> getOldBackups(long cutoffTime);

    @Query("SELECT * FROM backup_records WHERE backup_type = 'local' AND status = 'completed' ORDER BY created_at DESC")
    List<BackupRecord> getLocalCompletedBackups();

    @Query("SELECT * FROM backup_records WHERE backup_type = 'drive' AND status = 'completed' ORDER BY created_at DESC")
    List<BackupRecord> getDriveCompletedBackups();
}
