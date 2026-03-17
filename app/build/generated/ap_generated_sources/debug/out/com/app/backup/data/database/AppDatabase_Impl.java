package com.app.backup.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.app.backup.data.database.dao.BackupDao;
import com.app.backup.data.database.dao.BackupDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BackupDao _backupDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `backup_records` (`backup_id` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `backup_date_display` TEXT, `contacts_count` INTEGER NOT NULL, `sms_count` INTEGER NOT NULL, `file_path` TEXT, `file_size_bytes` INTEGER NOT NULL, `status` TEXT, `backup_type` TEXT, `drive_file_id` TEXT, `drive_file_name` TEXT, `error_message` TEXT, `device_name` TEXT, `app_version` TEXT, `checksum` TEXT, `is_encrypted` INTEGER NOT NULL, `includes_contacts` INTEGER NOT NULL, `includes_sms` INTEGER NOT NULL, PRIMARY KEY(`backup_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '69c2ca70072a34434a3803aa5966225d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `backup_records`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBackupRecords = new HashMap<String, TableInfo.Column>(18);
        _columnsBackupRecords.put("backup_id", new TableInfo.Column("backup_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("backup_date_display", new TableInfo.Column("backup_date_display", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("contacts_count", new TableInfo.Column("contacts_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("sms_count", new TableInfo.Column("sms_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("file_path", new TableInfo.Column("file_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("file_size_bytes", new TableInfo.Column("file_size_bytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("backup_type", new TableInfo.Column("backup_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("drive_file_id", new TableInfo.Column("drive_file_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("drive_file_name", new TableInfo.Column("drive_file_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("error_message", new TableInfo.Column("error_message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("device_name", new TableInfo.Column("device_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("app_version", new TableInfo.Column("app_version", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("checksum", new TableInfo.Column("checksum", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("is_encrypted", new TableInfo.Column("is_encrypted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("includes_contacts", new TableInfo.Column("includes_contacts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackupRecords.put("includes_sms", new TableInfo.Column("includes_sms", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBackupRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBackupRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBackupRecords = new TableInfo("backup_records", _columnsBackupRecords, _foreignKeysBackupRecords, _indicesBackupRecords);
        final TableInfo _existingBackupRecords = TableInfo.read(db, "backup_records");
        if (!_infoBackupRecords.equals(_existingBackupRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "backup_records(com.app.backup.data.database.entity.BackupRecord).\n"
                  + " Expected:\n" + _infoBackupRecords + "\n"
                  + " Found:\n" + _existingBackupRecords);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "69c2ca70072a34434a3803aa5966225d", "be257f2aca34dab1e41ff7cfaf08a2a5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "backup_records");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `backup_records`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BackupDao.class, BackupDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BackupDao backupDao() {
    if (_backupDao != null) {
      return _backupDao;
    } else {
      synchronized(this) {
        if(_backupDao == null) {
          _backupDao = new BackupDao_Impl(this);
        }
        return _backupDao;
      }
    }
  }
}
