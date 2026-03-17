package com.app.backup.di;

import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.repository.BackupRepository;
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
public final class AppModule_ProvideBackupRepositoryFactory implements Factory<BackupRepository> {
  private final AppModule module;

  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideBackupRepositoryFactory(AppModule module,
      Provider<AppDatabase> databaseProvider) {
    this.module = module;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupRepository get() {
    return provideBackupRepository(module, databaseProvider.get());
  }

  public static AppModule_ProvideBackupRepositoryFactory create(AppModule module,
      Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideBackupRepositoryFactory(module, databaseProvider);
  }

  public static BackupRepository provideBackupRepository(AppModule instance, AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(instance.provideBackupRepository(database));
  }
}
