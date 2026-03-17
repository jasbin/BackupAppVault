package com.app.backup.di;

import android.content.Context;

import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.dao.BackupDao;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.data.restore.RestoreManager;
import com.app.backup.utils.PermissionsUtil;
import com.app.backup.utils.security.EncryptionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public BackupDao provideBackupDao(AppDatabase database) {
        return database.backupDao();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    @Singleton
    public EncryptionUtil provideEncryptionUtil() {
        return new EncryptionUtil();
    }

    @Provides
    @Singleton
    public ContactsRepository provideContactsRepository(@ApplicationContext Context context) {
        return new ContactsRepository(context);
    }

    @Provides
    @Singleton
    public SmsRepository provideSmsRepository(@ApplicationContext Context context) {
        return new SmsRepository(context);
    }

    @Provides
    @Singleton
    public BackupRepository provideBackupRepository(AppDatabase database) {
        return new BackupRepository(database);
    }

    @Provides
    @Singleton
    public BackupManager provideBackupManager(@ApplicationContext Context context,
                                               ContactsRepository contactsRepository,
                                               SmsRepository smsRepository,
                                               EncryptionUtil encryptionUtil,
                                               AppDatabase database) {
        return new BackupManager(context, contactsRepository, smsRepository, encryptionUtil, database);
    }

    @Provides
    @Singleton
    public RestoreManager provideRestoreManager(ContactsRepository contactsRepository,
                                                SmsRepository smsRepository,
                                                EncryptionUtil encryptionUtil) {
        return new RestoreManager(contactsRepository, smsRepository, encryptionUtil);
    }

    @Provides
    @Singleton
    public DriveService provideDriveService(@ApplicationContext Context context, AppDatabase database) {
        return new DriveService(context, database);
    }

    @Provides
    @Singleton
    public PermissionsUtil providePermissionsUtil() {
        return new PermissionsUtil();
    }
}
