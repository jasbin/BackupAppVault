package com.app.backup.di;

import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.dao.BackupDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideBackupDaoFactory implements Factory<BackupDao> {
  private final AppModule module;

  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideBackupDaoFactory(AppModule module,
      Provider<AppDatabase> databaseProvider) {
    this.module = module;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupDao get() {
    return provideBackupDao(module, databaseProvider.get());
  }

  public static AppModule_ProvideBackupDaoFactory create(AppModule module,
      Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideBackupDaoFactory(module, databaseProvider);
  }

  public static BackupDao provideBackupDao(AppModule instance, AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(instance.provideBackupDao(database));
  }
}
