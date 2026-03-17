package com.app.backup.di;

import android.content.Context;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideBackupManagerFactory implements Factory<BackupManager> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<SmsRepository> smsRepositoryProvider;

  private final Provider<EncryptionUtil> encryptionUtilProvider;

  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideBackupManagerFactory(AppModule module, Provider<Context> contextProvider,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider, Provider<AppDatabase> databaseProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.smsRepositoryProvider = smsRepositoryProvider;
    this.encryptionUtilProvider = encryptionUtilProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupManager get() {
    return provideBackupManager(module, contextProvider.get(), contactsRepositoryProvider.get(), smsRepositoryProvider.get(), encryptionUtilProvider.get(), databaseProvider.get());
  }

  public static AppModule_ProvideBackupManagerFactory create(AppModule module,
      Provider<Context> contextProvider, Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider, Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideBackupManagerFactory(module, contextProvider, contactsRepositoryProvider, smsRepositoryProvider, encryptionUtilProvider, databaseProvider);
  }

  public static BackupManager provideBackupManager(AppModule instance, Context context,
      ContactsRepository contactsRepository, SmsRepository smsRepository,
      EncryptionUtil encryptionUtil, AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(instance.provideBackupManager(context, contactsRepository, smsRepository, encryptionUtil, database));
  }
}
