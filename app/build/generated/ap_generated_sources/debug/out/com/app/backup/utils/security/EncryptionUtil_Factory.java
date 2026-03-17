package com.app.backup.utils.security;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class EncryptionUtil_Factory implements Factory<EncryptionUtil> {
  @Override
  public EncryptionUtil get() {
    return newInstance();
  }

  public static EncryptionUtil_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EncryptionUtil newInstance() {
    return new EncryptionUtil();
  }

  private static final class InstanceHolder {
    private static final EncryptionUtil_Factory INSTANCE = new EncryptionUtil_Factory();
  }
}
