package com.app.backup.viewmodel;

import android.content.Context;
import com.app.backup.data.drive.DriveService;
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
public final class DriveViewModel_Factory implements Factory<DriveViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<DriveService> driveServiceProvider;

  public DriveViewModel_Factory(Provider<Context> contextProvider,
      Provider<DriveService> driveServiceProvider) {
    this.contextProvider = contextProvider;
    this.driveServiceProvider = driveServiceProvider;
  }

  @Override
  public DriveViewModel get() {
    return newInstance(contextProvider.get(), driveServiceProvider.get());
  }

  public static DriveViewModel_Factory create(Provider<Context> contextProvider,
      Provider<DriveService> driveServiceProvider) {
    return new DriveViewModel_Factory(contextProvider, driveServiceProvider);
  }

  public static DriveViewModel newInstance(Context context, DriveService driveService) {
    return new DriveViewModel(context, driveService);
  }
}
