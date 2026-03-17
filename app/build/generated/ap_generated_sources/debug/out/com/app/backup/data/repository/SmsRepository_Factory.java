package com.app.backup.data.repository;

import android.content.Context;
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
public final class SmsRepository_Factory implements Factory<SmsRepository> {
  private final Provider<Context> contextProvider;

  public SmsRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SmsRepository get() {
    return newInstance(contextProvider.get());
  }

  public static SmsRepository_Factory create(Provider<Context> contextProvider) {
    return new SmsRepository_Factory(contextProvider);
  }

  public static SmsRepository newInstance(Context context) {
    return new SmsRepository(context);
  }
}
