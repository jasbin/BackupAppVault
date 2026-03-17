package com.app.backup.data.backup;

import android.content.Context;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class BackupManager_Factory implements Factory<BackupManager> {
  private final Provider<Context> contextProvider;

  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<SmsRepository> smsRepositoryProvider;

  private final Provider<EncryptionUtil> encryptionUtilProvider;

  private final Provider<AppDatabase> databaseProvider;

  public BackupManager_Factory(Provider<Context> contextProvider,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider, Provider<AppDatabase> databaseProvider) {
    this.contextProvider = contextProvider;
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.smsRepositoryProvider = smsRepositoryProvider;
    this.encryptionUtilProvider = encryptionUtilProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupManager get() {
    return newInstance(contextProvider.get(), contactsRepositoryProvider.get(), smsRepositoryProvider.get(), encryptionUtilProvider.get(), databaseProvider.get());
  }

  public static BackupManager_Factory create(Provider<Context> contextProvider,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider, Provider<AppDatabase> databaseProvider) {
    return new BackupManager_Factory(contextProvider, contactsRepositoryProvider, smsRepositoryProvider, encryptionUtilProvider, databaseProvider);
  }

  public static BackupManager newInstance(Context context, ContactsRepository contactsRepository,
      SmsRepository smsRepository, EncryptionUtil encryptionUtil, AppDatabase database) {
    return new BackupManager(context, contactsRepository, smsRepository, encryptionUtil, database);
  }
}
