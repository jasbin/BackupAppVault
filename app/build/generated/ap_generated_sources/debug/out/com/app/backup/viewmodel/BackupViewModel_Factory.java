package com.app.backup.viewmodel;

import android.content.Context;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
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
public final class BackupViewModel_Factory implements Factory<BackupViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<BackupManager> backupManagerProvider;

  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<DriveService> driveServiceProvider;

  public BackupViewModel_Factory(Provider<Context> contextProvider,
      Provider<BackupManager> backupManagerProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<DriveService> driveServiceProvider) {
    this.contextProvider = contextProvider;
    this.backupManagerProvider = backupManagerProvider;
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.driveServiceProvider = driveServiceProvider;
  }

  @Override
  public BackupViewModel get() {
    return newInstance(contextProvider.get(), backupManagerProvider.get(), backupRepositoryProvider.get(), driveServiceProvider.get());
  }

  public static BackupViewModel_Factory create(Provider<Context> contextProvider,
      Provider<BackupManager> backupManagerProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<DriveService> driveServiceProvider) {
    return new BackupViewModel_Factory(contextProvider, backupManagerProvider, backupRepositoryProvider, driveServiceProvider);
  }

  public static BackupViewModel newInstance(Context context, BackupManager backupManager,
      BackupRepository backupRepository, DriveService driveService) {
    return new BackupViewModel(context, backupManager, backupRepository, driveService);
  }
}
