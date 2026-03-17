package com.app.backup.di;

import android.content.Context;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.drive.DriveService;
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
public final class AppModule_ProvideDriveServiceFactory implements Factory<DriveService> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideDriveServiceFactory(AppModule module, Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DriveService get() {
    return provideDriveService(module, contextProvider.get(), databaseProvider.get());
  }

  public static AppModule_ProvideDriveServiceFactory create(AppModule module,
      Provider<Context> contextProvider, Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideDriveServiceFactory(module, contextProvider, databaseProvider);
  }

  public static DriveService provideDriveService(AppModule instance, Context context,
      AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(instance.provideDriveService(context, database));
  }
}
