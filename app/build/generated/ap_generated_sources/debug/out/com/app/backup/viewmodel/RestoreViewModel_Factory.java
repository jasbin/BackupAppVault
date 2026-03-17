package com.app.backup.viewmodel;

import android.content.Context;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.restore.RestoreManager;
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
public final class RestoreViewModel_Factory implements Factory<RestoreViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<RestoreManager> restoreManagerProvider;

  private final Provider<BackupRepository> backupRepositoryProvider;

  private final Provider<DriveService> driveServiceProvider;

  public RestoreViewModel_Factory(Provider<Context> contextProvider,
      Provider<RestoreManager> restoreManagerProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<DriveService> driveServiceProvider) {
    this.contextProvider = contextProvider;
    this.restoreManagerProvider = restoreManagerProvider;
    this.backupRepositoryProvider = backupRepositoryProvider;
    this.driveServiceProvider = driveServiceProvider;
  }

  @Override
  public RestoreViewModel get() {
    return newInstance(contextProvider.get(), restoreManagerProvider.get(), backupRepositoryProvider.get(), driveServiceProvider.get());
  }

  public static RestoreViewModel_Factory create(Provider<Context> contextProvider,
      Provider<RestoreManager> restoreManagerProvider,
      Provider<BackupRepository> backupRepositoryProvider,
      Provider<DriveService> driveServiceProvider) {
    return new RestoreViewModel_Factory(contextProvider, restoreManagerProvider, backupRepositoryProvider, driveServiceProvider);
  }

  public static RestoreViewModel newInstance(Context context, RestoreManager restoreManager,
      BackupRepository backupRepository, DriveService driveService) {
    return new RestoreViewModel(context, restoreManager, backupRepository, driveService);
  }
}
