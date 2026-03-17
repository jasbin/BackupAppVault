package com.app.backup.viewmodel;

import android.content.Context;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<SmsRepository> smsRepositoryProvider;

  private final Provider<BackupManager> backupManagerProvider;

  public DashboardViewModel_Factory(Provider<Context> contextProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<BackupManager> backupManagerProvider) {
    this.contextProvider = contextProvider;
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.smsRepositoryProvider = smsRepositoryProvider;
    this.backupManagerProvider = backupManagerProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(contextProvider.get(), backupRepositoryProvider.get(), contactsRepositoryProvider.get(), smsRepositoryProvider.get(), backupManagerProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Context> contextProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<BackupManager> backupManagerProvider) {
    return new DashboardViewModel_Factory(contextProvider, backupRepositoryProvider, contactsRepositoryProvider, smsRepositoryProvider, backupManagerProvider);
  }

  public static DashboardViewModel newInstance(Context context, BackupRepository backupRepository,
      ContactsRepository contactsRepository, SmsRepository smsRepository,
      BackupManager backupManager) {
    return new DashboardViewModel(context, backupRepository, contactsRepository, smsRepository, backupManager);
  }
}
