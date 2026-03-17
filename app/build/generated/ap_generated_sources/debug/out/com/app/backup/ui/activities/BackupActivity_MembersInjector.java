package com.app.backup.ui.activities;

import com.app.backup.data.drive.DriveService;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BackupActivity_MembersInjector implements MembersInjector<BackupActivity> {
  private final Provider<DriveService> driveServiceProvider;

  public BackupActivity_MembersInjector(Provider<DriveService> driveServiceProvider) {
    this.driveServiceProvider = driveServiceProvider;
  }

  public static MembersInjector<BackupActivity> create(
      Provider<DriveService> driveServiceProvider) {
    return new BackupActivity_MembersInjector(driveServiceProvider);
  }

  @Override
  public void injectMembers(BackupActivity instance) {
    injectDriveService(instance, driveServiceProvider.get());
  }

  @InjectedFieldSignature("com.app.backup.ui.activities.BackupActivity.driveService")
  public static void injectDriveService(BackupActivity instance, DriveService driveService) {
    instance.driveService = driveService;
  }
}
