package com.app.backup.data.drive;

import android.content.Context;
import com.app.backup.data.database.AppDatabase;
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
public final class DriveService_Factory implements Factory<DriveService> {
  private final Provider<Context> contextProvider;

  private final Provider<AppDatabase> databaseProvider;

  public DriveService_Factory(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider) {
    this.contextProvider = contextProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DriveService get() {
    return newInstance(contextProvider.get(), databaseProvider.get());
  }

  public static DriveService_Factory create(Provider<Context> contextProvider,
      Provider<AppDatabase> databaseProvider) {
    return new DriveService_Factory(contextProvider, databaseProvider);
  }

  public static DriveService newInstance(Context context, AppDatabase database) {
    return new DriveService(context, database);
  }
}
