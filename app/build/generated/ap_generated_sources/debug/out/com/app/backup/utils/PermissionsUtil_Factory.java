package com.app.backup.utils;

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
public final class PermissionsUtil_Factory implements Factory<PermissionsUtil> {
  @Override
  public PermissionsUtil get() {
    return newInstance();
  }

  public static PermissionsUtil_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PermissionsUtil newInstance() {
    return new PermissionsUtil();
  }

  private static final class InstanceHolder {
    private static final PermissionsUtil_Factory INSTANCE = new PermissionsUtil_Factory();
  }
}
