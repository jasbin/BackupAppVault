package com.app.backup.data.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.app.backup.data.database.entity.BackupRecord;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BackupDao_Impl implements BackupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BackupRecord> __insertionAdapterOfBackupRecord;

  private final EntityDeletionOrUpdateAdapter<BackupRecord> __deletionAdapterOfBackupRecord;

  private final EntityDeletionOrUpdateAdapter<BackupRecord> __updateAdapterOfBackupRecord;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBackupById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteFailedBackups;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldBackups;

  public BackupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBackupRecord = new EntityInsertionAdapter<BackupRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `backup_records` (`backup_id`,`created_at`,`backup_date_display`,`contacts_count`,`sms_count`,`file_path`,`file_size_bytes`,`status`,`backup_type`,`drive_file_id`,`drive_file_name`,`error_message`,`device_name`,`app_version`,`checksum`,`is_encrypted`,`includes_contacts`,`includes_sms`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BackupRecord entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
        statement.bindLong(2, entity.getCreatedAt());
        if (entity.getBackupDateDisplay() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBackupDateDisplay());
        }
        statement.bindLong(4, entity.getContactsCount());
        statement.bindLong(5, entity.getSmsCount());
        if (entity.getFilePath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFilePath());
        }
        statement.bindLong(7, entity.getFileSizeBytes());
        if (entity.getStatus() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getStatus());
        }
        if (entity.getBackupType() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getBackupType());
        }
        if (entity.getDriveFileId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDriveFileId());
        }
        if (entity.getDriveFileName() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDriveFileName());
        }
        if (entity.getErrorMessage() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getErrorMessage());
        }
        if (entity.getDeviceName() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getDeviceName());
        }
        if (entity.getAppVersion() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getAppVersion());
        }
        if (entity.getChecksum() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getChecksum());
        }
        final int _tmp = entity.isEncrypted() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final int _tmp_1 = entity.isIncludesContacts() ? 1 : 0;
        statement.bindLong(17, _tmp_1);
        final int _tmp_2 = entity.isIncludesSms() ? 1 : 0;
        statement.bindLong(18, _tmp_2);
      }
    };
    this.__deletionAdapterOfBackupRecord = new EntityDeletionOrUpdateAdapter<BackupRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `backup_records` WHERE `backup_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BackupRecord entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
      }
    };
    this.__updateAdapterOfBackupRecord = new EntityDeletionOrUpdateAdapter<BackupRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `backup_records` SET `backup_id` = ?,`created_at` = ?,`backup_date_display` = ?,`contacts_count` = ?,`sms_count` = ?,`file_path` = ?,`file_size_bytes` = ?,`status` = ?,`backup_type` = ?,`drive_file_id` = ?,`drive_file_name` = ?,`error_message` = ?,`device_name` = ?,`app_version` = ?,`checksum` = ?,`is_encrypted` = ?,`includes_contacts` = ?,`includes_sms` = ? WHERE `backup_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BackupRecord entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
        statement.bindLong(2, entity.getCreatedAt());
        if (entity.getBackupDateDisplay() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBackupDateDisplay());
        }
        statement.bindLong(4, entity.getContactsCount());
        statement.bindLong(5, entity.getSmsCount());
        if (entity.getFilePath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFilePath());
        }
        statement.bindLong(7, entity.getFileSizeBytes());
        if (entity.getStatus() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getStatus());
        }
        if (entity.getBackupType() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getBackupType());
        }
        if (entity.getDriveFileId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDriveFileId());
        }
        if (entity.getDriveFileName() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDriveFileName());
        }
        if (entity.getErrorMessage() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getErrorMessage());
        }
        if (entity.getDeviceName() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getDeviceName());
        }
        if (entity.getAppVersion() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getAppVersion());
        }
        if (entity.getChecksum() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getChecksum());
        }
        final int _tmp = entity.isEncrypted() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final int _tmp_1 = entity.isIncludesContacts() ? 1 : 0;
        statement.bindLong(17, _tmp_1);
        final int _tmp_2 = entity.isIncludesSms() ? 1 : 0;
        statement.bindLong(18, _tmp_2);
        if (entity.getBackupId() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getBackupId());
        }
      }
    };
    this.__preparedStmtOfDeleteBackupById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backup_records WHERE backup_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteFailedBackups = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backup_records WHERE status = 'failed'";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldBackups = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backup_records WHERE created_at < ?";
        return _query;
      }
    };
  }

  @Override
  public void insertBackup(final BackupRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBackupRecord.insert(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBackup(final BackupRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBackupRecord.handle(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBackup(final BackupRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfBackupRecord.handle(record);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBackupById(final String id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBackupById.acquire();
    int _argIndex = 1;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteBackupById.release(_stmt);
    }
  }

  @Override
  public void deleteFailedBackups() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFailedBackups.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteFailedBackups.release(_stmt);
    }
  }

  @Override
  public void deleteOldBackups(final long cutoffTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldBackups.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, cutoffTime);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOldBackups.release(_stmt);
    }
  }

  @Override
  public LiveData<List<BackupRecord>> getAllBackupsLive() {
    final String _sql = "SELECT * FROM backup_records ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"backup_records"}, false, new Callable<List<BackupRecord>>() {
      @Override
      @Nullable
      public List<BackupRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
          final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
          final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
          final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
          final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
          final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
          final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
          final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
          final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
          final List<BackupRecord> _result = new ArrayList<BackupRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BackupRecord _item;
            final String _tmpBackupId;
            if (_cursor.isNull(_cursorIndexOfBackupId)) {
              _tmpBackupId = null;
            } else {
              _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
            }
            _item = new BackupRecord(_tmpBackupId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final String _tmpBackupDateDisplay;
            if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
              _tmpBackupDateDisplay = null;
            } else {
              _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
            }
            _item.setBackupDateDisplay(_tmpBackupDateDisplay);
            final int _tmpContactsCount;
            _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
            _item.setContactsCount(_tmpContactsCount);
            final int _tmpSmsCount;
            _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
            _item.setSmsCount(_tmpSmsCount);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            _item.setFilePath(_tmpFilePath);
            final long _tmpFileSizeBytes;
            _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
            _item.setFileSizeBytes(_tmpFileSizeBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpBackupType;
            if (_cursor.isNull(_cursorIndexOfBackupType)) {
              _tmpBackupType = null;
            } else {
              _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
            }
            _item.setBackupType(_tmpBackupType);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            _item.setDriveFileId(_tmpDriveFileId);
            final String _tmpDriveFileName;
            if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
              _tmpDriveFileName = null;
            } else {
              _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
            }
            _item.setDriveFileName(_tmpDriveFileName);
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item.setErrorMessage(_tmpErrorMessage);
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            _item.setDeviceName(_tmpDeviceName);
            final String _tmpAppVersion;
            if (_cursor.isNull(_cursorIndexOfAppVersion)) {
              _tmpAppVersion = null;
            } else {
              _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
            }
            _item.setAppVersion(_tmpAppVersion);
            final String _tmpChecksum;
            if (_cursor.isNull(_cursorIndexOfChecksum)) {
              _tmpChecksum = null;
            } else {
              _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
            }
            _item.setChecksum(_tmpChecksum);
            final boolean _tmpIsEncrypted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
            _tmpIsEncrypted = _tmp != 0;
            _item.setEncrypted(_tmpIsEncrypted);
            final boolean _tmpIncludesContacts;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
            _tmpIncludesContacts = _tmp_1 != 0;
            _item.setIncludesContacts(_tmpIncludesContacts);
            final boolean _tmpIncludesSms;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
            _tmpIncludesSms = _tmp_2 != 0;
            _item.setIncludesSms(_tmpIncludesSms);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<BackupRecord> getAllBackups() {
    final String _sql = "SELECT * FROM backup_records ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
      final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
      final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
      final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
      final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
      final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
      final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
      final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
      final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
      final List<BackupRecord> _result = new ArrayList<BackupRecord>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BackupRecord _item;
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item = new BackupRecord(_tmpBackupId);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final String _tmpBackupDateDisplay;
        if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
          _tmpBackupDateDisplay = null;
        } else {
          _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
        }
        _item.setBackupDateDisplay(_tmpBackupDateDisplay);
        final int _tmpContactsCount;
        _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
        _item.setContactsCount(_tmpContactsCount);
        final int _tmpSmsCount;
        _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
        _item.setSmsCount(_tmpSmsCount);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final long _tmpFileSizeBytes;
        _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
        _item.setFileSizeBytes(_tmpFileSizeBytes);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpBackupType;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmpBackupType = null;
        } else {
          _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
        }
        _item.setBackupType(_tmpBackupType);
        final String _tmpDriveFileId;
        if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
          _tmpDriveFileId = null;
        } else {
          _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
        }
        _item.setDriveFileId(_tmpDriveFileId);
        final String _tmpDriveFileName;
        if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
          _tmpDriveFileName = null;
        } else {
          _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
        }
        _item.setDriveFileName(_tmpDriveFileName);
        final String _tmpErrorMessage;
        if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
          _tmpErrorMessage = null;
        } else {
          _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
        }
        _item.setErrorMessage(_tmpErrorMessage);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChecksum;
        if (_cursor.isNull(_cursorIndexOfChecksum)) {
          _tmpChecksum = null;
        } else {
          _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
        }
        _item.setChecksum(_tmpChecksum);
        final boolean _tmpIsEncrypted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final boolean _tmpIncludesContacts;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
        _tmpIncludesContacts = _tmp_1 != 0;
        _item.setIncludesContacts(_tmpIncludesContacts);
        final boolean _tmpIncludesSms;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
        _tmpIncludesSms = _tmp_2 != 0;
        _item.setIncludesSms(_tmpIncludesSms);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public BackupRecord getBackupById(final String id) {
    final String _sql = "SELECT * FROM backup_records WHERE backup_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
      final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
      final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
      final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
      final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
      final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
      final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
      final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
      final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
      final BackupRecord _result;
      if (_cursor.moveToFirst()) {
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _result = new BackupRecord(_tmpBackupId);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result.setCreatedAt(_tmpCreatedAt);
        final String _tmpBackupDateDisplay;
        if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
          _tmpBackupDateDisplay = null;
        } else {
          _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
        }
        _result.setBackupDateDisplay(_tmpBackupDateDisplay);
        final int _tmpContactsCount;
        _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
        _result.setContactsCount(_tmpContactsCount);
        final int _tmpSmsCount;
        _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
        _result.setSmsCount(_tmpSmsCount);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _result.setFilePath(_tmpFilePath);
        final long _tmpFileSizeBytes;
        _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
        _result.setFileSizeBytes(_tmpFileSizeBytes);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpBackupType;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmpBackupType = null;
        } else {
          _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
        }
        _result.setBackupType(_tmpBackupType);
        final String _tmpDriveFileId;
        if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
          _tmpDriveFileId = null;
        } else {
          _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
        }
        _result.setDriveFileId(_tmpDriveFileId);
        final String _tmpDriveFileName;
        if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
          _tmpDriveFileName = null;
        } else {
          _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
        }
        _result.setDriveFileName(_tmpDriveFileName);
        final String _tmpErrorMessage;
        if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
          _tmpErrorMessage = null;
        } else {
          _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
        }
        _result.setErrorMessage(_tmpErrorMessage);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _result.setDeviceName(_tmpDeviceName);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
        final String _tmpChecksum;
        if (_cursor.isNull(_cursorIndexOfChecksum)) {
          _tmpChecksum = null;
        } else {
          _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
        }
        _result.setChecksum(_tmpChecksum);
        final boolean _tmpIsEncrypted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp != 0;
        _result.setEncrypted(_tmpIsEncrypted);
        final boolean _tmpIncludesContacts;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
        _tmpIncludesContacts = _tmp_1 != 0;
        _result.setIncludesContacts(_tmpIncludesContacts);
        final boolean _tmpIncludesSms;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
        _tmpIncludesSms = _tmp_2 != 0;
        _result.setIncludesSms(_tmpIncludesSms);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<BackupRecord>> getBackupsByType(final String type) {
    final String _sql = "SELECT * FROM backup_records WHERE backup_type = ? ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"backup_records"}, false, new Callable<List<BackupRecord>>() {
      @Override
      @Nullable
      public List<BackupRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
          final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
          final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
          final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
          final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
          final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
          final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
          final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
          final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
          final List<BackupRecord> _result = new ArrayList<BackupRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BackupRecord _item;
            final String _tmpBackupId;
            if (_cursor.isNull(_cursorIndexOfBackupId)) {
              _tmpBackupId = null;
            } else {
              _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
            }
            _item = new BackupRecord(_tmpBackupId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final String _tmpBackupDateDisplay;
            if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
              _tmpBackupDateDisplay = null;
            } else {
              _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
            }
            _item.setBackupDateDisplay(_tmpBackupDateDisplay);
            final int _tmpContactsCount;
            _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
            _item.setContactsCount(_tmpContactsCount);
            final int _tmpSmsCount;
            _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
            _item.setSmsCount(_tmpSmsCount);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            _item.setFilePath(_tmpFilePath);
            final long _tmpFileSizeBytes;
            _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
            _item.setFileSizeBytes(_tmpFileSizeBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpBackupType;
            if (_cursor.isNull(_cursorIndexOfBackupType)) {
              _tmpBackupType = null;
            } else {
              _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
            }
            _item.setBackupType(_tmpBackupType);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            _item.setDriveFileId(_tmpDriveFileId);
            final String _tmpDriveFileName;
            if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
              _tmpDriveFileName = null;
            } else {
              _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
            }
            _item.setDriveFileName(_tmpDriveFileName);
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item.setErrorMessage(_tmpErrorMessage);
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            _item.setDeviceName(_tmpDeviceName);
            final String _tmpAppVersion;
            if (_cursor.isNull(_cursorIndexOfAppVersion)) {
              _tmpAppVersion = null;
            } else {
              _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
            }
            _item.setAppVersion(_tmpAppVersion);
            final String _tmpChecksum;
            if (_cursor.isNull(_cursorIndexOfChecksum)) {
              _tmpChecksum = null;
            } else {
              _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
            }
            _item.setChecksum(_tmpChecksum);
            final boolean _tmpIsEncrypted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
            _tmpIsEncrypted = _tmp != 0;
            _item.setEncrypted(_tmpIsEncrypted);
            final boolean _tmpIncludesContacts;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
            _tmpIncludesContacts = _tmp_1 != 0;
            _item.setIncludesContacts(_tmpIncludesContacts);
            final boolean _tmpIncludesSms;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
            _tmpIncludesSms = _tmp_2 != 0;
            _item.setIncludesSms(_tmpIncludesSms);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public BackupRecord getLatestCompletedBackup() {
    final String _sql = "SELECT * FROM backup_records WHERE status = 'completed' ORDER BY created_at DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
      final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
      final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
      final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
      final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
      final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
      final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
      final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
      final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
      final BackupRecord _result;
      if (_cursor.moveToFirst()) {
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _result = new BackupRecord(_tmpBackupId);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result.setCreatedAt(_tmpCreatedAt);
        final String _tmpBackupDateDisplay;
        if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
          _tmpBackupDateDisplay = null;
        } else {
          _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
        }
        _result.setBackupDateDisplay(_tmpBackupDateDisplay);
        final int _tmpContactsCount;
        _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
        _result.setContactsCount(_tmpContactsCount);
        final int _tmpSmsCount;
        _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
        _result.setSmsCount(_tmpSmsCount);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _result.setFilePath(_tmpFilePath);
        final long _tmpFileSizeBytes;
        _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
        _result.setFileSizeBytes(_tmpFileSizeBytes);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpBackupType;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmpBackupType = null;
        } else {
          _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
        }
        _result.setBackupType(_tmpBackupType);
        final String _tmpDriveFileId;
        if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
          _tmpDriveFileId = null;
        } else {
          _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
        }
        _result.setDriveFileId(_tmpDriveFileId);
        final String _tmpDriveFileName;
        if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
          _tmpDriveFileName = null;
        } else {
          _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
        }
        _result.setDriveFileName(_tmpDriveFileName);
        final String _tmpErrorMessage;
        if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
          _tmpErrorMessage = null;
        } else {
          _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
        }
        _result.setErrorMessage(_tmpErrorMessage);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _result.setDeviceName(_tmpDeviceName);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _result.setAppVersion(_tmpAppVersion);
        final String _tmpChecksum;
        if (_cursor.isNull(_cursorIndexOfChecksum)) {
          _tmpChecksum = null;
        } else {
          _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
        }
        _result.setChecksum(_tmpChecksum);
        final boolean _tmpIsEncrypted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp != 0;
        _result.setEncrypted(_tmpIsEncrypted);
        final boolean _tmpIncludesContacts;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
        _tmpIncludesContacts = _tmp_1 != 0;
        _result.setIncludesContacts(_tmpIncludesContacts);
        final boolean _tmpIncludesSms;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
        _tmpIncludesSms = _tmp_2 != 0;
        _result.setIncludesSms(_tmpIncludesSms);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<BackupRecord> getLatestCompletedBackupLive() {
    final String _sql = "SELECT * FROM backup_records WHERE status = 'completed' ORDER BY created_at DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"backup_records"}, false, new Callable<BackupRecord>() {
      @Override
      @Nullable
      public BackupRecord call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
          final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
          final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
          final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
          final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
          final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
          final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
          final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
          final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
          final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
          final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
          final BackupRecord _result;
          if (_cursor.moveToFirst()) {
            final String _tmpBackupId;
            if (_cursor.isNull(_cursorIndexOfBackupId)) {
              _tmpBackupId = null;
            } else {
              _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
            }
            _result = new BackupRecord(_tmpBackupId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.setCreatedAt(_tmpCreatedAt);
            final String _tmpBackupDateDisplay;
            if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
              _tmpBackupDateDisplay = null;
            } else {
              _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
            }
            _result.setBackupDateDisplay(_tmpBackupDateDisplay);
            final int _tmpContactsCount;
            _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
            _result.setContactsCount(_tmpContactsCount);
            final int _tmpSmsCount;
            _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
            _result.setSmsCount(_tmpSmsCount);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            _result.setFilePath(_tmpFilePath);
            final long _tmpFileSizeBytes;
            _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
            _result.setFileSizeBytes(_tmpFileSizeBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _result.setStatus(_tmpStatus);
            final String _tmpBackupType;
            if (_cursor.isNull(_cursorIndexOfBackupType)) {
              _tmpBackupType = null;
            } else {
              _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
            }
            _result.setBackupType(_tmpBackupType);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            _result.setDriveFileId(_tmpDriveFileId);
            final String _tmpDriveFileName;
            if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
              _tmpDriveFileName = null;
            } else {
              _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
            }
            _result.setDriveFileName(_tmpDriveFileName);
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _result.setErrorMessage(_tmpErrorMessage);
            final String _tmpDeviceName;
            if (_cursor.isNull(_cursorIndexOfDeviceName)) {
              _tmpDeviceName = null;
            } else {
              _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
            }
            _result.setDeviceName(_tmpDeviceName);
            final String _tmpAppVersion;
            if (_cursor.isNull(_cursorIndexOfAppVersion)) {
              _tmpAppVersion = null;
            } else {
              _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
            }
            _result.setAppVersion(_tmpAppVersion);
            final String _tmpChecksum;
            if (_cursor.isNull(_cursorIndexOfChecksum)) {
              _tmpChecksum = null;
            } else {
              _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
            }
            _result.setChecksum(_tmpChecksum);
            final boolean _tmpIsEncrypted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
            _tmpIsEncrypted = _tmp != 0;
            _result.setEncrypted(_tmpIsEncrypted);
            final boolean _tmpIncludesContacts;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
            _tmpIncludesContacts = _tmp_1 != 0;
            _result.setIncludesContacts(_tmpIncludesContacts);
            final boolean _tmpIncludesSms;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
            _tmpIncludesSms = _tmp_2 != 0;
            _result.setIncludesSms(_tmpIncludesSms);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getCompletedBackupCount() {
    final String _sql = "SELECT COUNT(*) FROM backup_records WHERE status = 'completed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public long getTotalBackupSize() {
    final String _sql = "SELECT SUM(file_size_bytes) FROM backup_records WHERE status = 'completed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final long _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getLong(0);
      } else {
        _result = 0L;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BackupRecord> getLocalCompletedBackups() {
    final String _sql = "SELECT * FROM backup_records WHERE backup_type = 'local' AND status = 'completed' ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
      final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
      final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
      final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
      final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
      final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
      final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
      final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
      final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
      final List<BackupRecord> _result = new ArrayList<BackupRecord>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BackupRecord _item;
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item = new BackupRecord(_tmpBackupId);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final String _tmpBackupDateDisplay;
        if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
          _tmpBackupDateDisplay = null;
        } else {
          _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
        }
        _item.setBackupDateDisplay(_tmpBackupDateDisplay);
        final int _tmpContactsCount;
        _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
        _item.setContactsCount(_tmpContactsCount);
        final int _tmpSmsCount;
        _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
        _item.setSmsCount(_tmpSmsCount);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final long _tmpFileSizeBytes;
        _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
        _item.setFileSizeBytes(_tmpFileSizeBytes);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpBackupType;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmpBackupType = null;
        } else {
          _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
        }
        _item.setBackupType(_tmpBackupType);
        final String _tmpDriveFileId;
        if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
          _tmpDriveFileId = null;
        } else {
          _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
        }
        _item.setDriveFileId(_tmpDriveFileId);
        final String _tmpDriveFileName;
        if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
          _tmpDriveFileName = null;
        } else {
          _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
        }
        _item.setDriveFileName(_tmpDriveFileName);
        final String _tmpErrorMessage;
        if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
          _tmpErrorMessage = null;
        } else {
          _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
        }
        _item.setErrorMessage(_tmpErrorMessage);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChecksum;
        if (_cursor.isNull(_cursorIndexOfChecksum)) {
          _tmpChecksum = null;
        } else {
          _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
        }
        _item.setChecksum(_tmpChecksum);
        final boolean _tmpIsEncrypted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final boolean _tmpIncludesContacts;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
        _tmpIncludesContacts = _tmp_1 != 0;
        _item.setIncludesContacts(_tmpIncludesContacts);
        final boolean _tmpIncludesSms;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
        _tmpIncludesSms = _tmp_2 != 0;
        _item.setIncludesSms(_tmpIncludesSms);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BackupRecord> getDriveCompletedBackups() {
    final String _sql = "SELECT * FROM backup_records WHERE backup_type = 'drive' AND status = 'completed' ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfBackupDateDisplay = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_date_display");
      final int _cursorIndexOfContactsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "contacts_count");
      final int _cursorIndexOfSmsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "sms_count");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "file_path");
      final int _cursorIndexOfFileSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size_bytes");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backup_type");
      final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_id");
      final int _cursorIndexOfDriveFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "drive_file_name");
      final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "error_message");
      final int _cursorIndexOfDeviceName = CursorUtil.getColumnIndexOrThrow(_cursor, "device_name");
      final int _cursorIndexOfAppVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "app_version");
      final int _cursorIndexOfChecksum = CursorUtil.getColumnIndexOrThrow(_cursor, "checksum");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_encrypted");
      final int _cursorIndexOfIncludesContacts = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_contacts");
      final int _cursorIndexOfIncludesSms = CursorUtil.getColumnIndexOrThrow(_cursor, "includes_sms");
      final List<BackupRecord> _result = new ArrayList<BackupRecord>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BackupRecord _item;
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item = new BackupRecord(_tmpBackupId);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final String _tmpBackupDateDisplay;
        if (_cursor.isNull(_cursorIndexOfBackupDateDisplay)) {
          _tmpBackupDateDisplay = null;
        } else {
          _tmpBackupDateDisplay = _cursor.getString(_cursorIndexOfBackupDateDisplay);
        }
        _item.setBackupDateDisplay(_tmpBackupDateDisplay);
        final int _tmpContactsCount;
        _tmpContactsCount = _cursor.getInt(_cursorIndexOfContactsCount);
        _item.setContactsCount(_tmpContactsCount);
        final int _tmpSmsCount;
        _tmpSmsCount = _cursor.getInt(_cursorIndexOfSmsCount);
        _item.setSmsCount(_tmpSmsCount);
        final String _tmpFilePath;
        if (_cursor.isNull(_cursorIndexOfFilePath)) {
          _tmpFilePath = null;
        } else {
          _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        }
        _item.setFilePath(_tmpFilePath);
        final long _tmpFileSizeBytes;
        _tmpFileSizeBytes = _cursor.getLong(_cursorIndexOfFileSizeBytes);
        _item.setFileSizeBytes(_tmpFileSizeBytes);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpBackupType;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmpBackupType = null;
        } else {
          _tmpBackupType = _cursor.getString(_cursorIndexOfBackupType);
        }
        _item.setBackupType(_tmpBackupType);
        final String _tmpDriveFileId;
        if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
          _tmpDriveFileId = null;
        } else {
          _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
        }
        _item.setDriveFileId(_tmpDriveFileId);
        final String _tmpDriveFileName;
        if (_cursor.isNull(_cursorIndexOfDriveFileName)) {
          _tmpDriveFileName = null;
        } else {
          _tmpDriveFileName = _cursor.getString(_cursorIndexOfDriveFileName);
        }
        _item.setDriveFileName(_tmpDriveFileName);
        final String _tmpErrorMessage;
        if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
          _tmpErrorMessage = null;
        } else {
          _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
        }
        _item.setErrorMessage(_tmpErrorMessage);
        final String _tmpDeviceName;
        if (_cursor.isNull(_cursorIndexOfDeviceName)) {
          _tmpDeviceName = null;
        } else {
          _tmpDeviceName = _cursor.getString(_cursorIndexOfDeviceName);
        }
        _item.setDeviceName(_tmpDeviceName);
        final String _tmpAppVersion;
        if (_cursor.isNull(_cursorIndexOfAppVersion)) {
          _tmpAppVersion = null;
        } else {
          _tmpAppVersion = _cursor.getString(_cursorIndexOfAppVersion);
        }
        _item.setAppVersion(_tmpAppVersion);
        final String _tmpChecksum;
        if (_cursor.isNull(_cursorIndexOfChecksum)) {
          _tmpChecksum = null;
        } else {
          _tmpChecksum = _cursor.getString(_cursorIndexOfChecksum);
        }
        _item.setChecksum(_tmpChecksum);
        final boolean _tmpIsEncrypted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final boolean _tmpIncludesContacts;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIncludesContacts);
        _tmpIncludesContacts = _tmp_1 != 0;
        _item.setIncludesContacts(_tmpIncludesContacts);
        final boolean _tmpIncludesSms;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIncludesSms);
        _tmpIncludesSms = _tmp_2 != 0;
        _item.setIncludesSms(_tmpIncludesSms);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
