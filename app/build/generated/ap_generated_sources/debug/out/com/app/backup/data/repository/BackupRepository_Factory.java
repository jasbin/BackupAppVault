package com.app.backup.data.repository;

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
public final class BackupRepository_Factory implements Factory<BackupRepository> {
  private final Provider<AppDatabase> databaseProvider;

  public BackupRepository_Factory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BackupRepository get() {
    return newInstance(databaseProvider.get());
  }

  public static BackupRepository_Factory create(Provider<AppDatabase> databaseProvider) {
    return new BackupRepository_Factory(databaseProvider);
  }

  public static BackupRepository newInstance(AppDatabase database) {
    return new BackupRepository(database);
  }
}
