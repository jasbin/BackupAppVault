package com.app.backup.di;

import android.content.Context;
import com.app.backup.data.repository.SmsRepository;
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
public final class AppModule_ProvideSmsRepositoryFactory implements Factory<SmsRepository> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  public AppModule_ProvideSmsRepositoryFactory(AppModule module,
      Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public SmsRepository get() {
    return provideSmsRepository(module, contextProvider.get());
  }

  public static AppModule_ProvideSmsRepositoryFactory create(AppModule module,
      Provider<Context> contextProvider) {
    return new AppModule_ProvideSmsRepositoryFactory(module, contextProvider);
  }

  public static SmsRepository provideSmsRepository(AppModule instance, Context context) {
    return Preconditions.checkNotNullFromProvides(instance.provideSmsRepository(context));
  }
}
