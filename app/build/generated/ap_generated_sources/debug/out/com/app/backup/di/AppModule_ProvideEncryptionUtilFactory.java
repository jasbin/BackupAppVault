package com.app.backup.di;

import com.app.backup.utils.security.EncryptionUtil;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideEncryptionUtilFactory implements Factory<EncryptionUtil> {
  private final AppModule module;

  public AppModule_ProvideEncryptionUtilFactory(AppModule module) {
    this.module = module;
  }

  @Override
  public EncryptionUtil get() {
    return provideEncryptionUtil(module);
  }

  public static AppModule_ProvideEncryptionUtilFactory create(AppModule module) {
    return new AppModule_ProvideEncryptionUtilFactory(module);
  }

  public static EncryptionUtil provideEncryptionUtil(AppModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideEncryptionUtil());
  }
}
