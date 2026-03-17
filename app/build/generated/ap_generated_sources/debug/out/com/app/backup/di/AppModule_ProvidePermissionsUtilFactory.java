package com.app.backup.di;

import com.app.backup.utils.PermissionsUtil;
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
public final class AppModule_ProvidePermissionsUtilFactory implements Factory<PermissionsUtil> {
  private final AppModule module;

  public AppModule_ProvidePermissionsUtilFactory(AppModule module) {
    this.module = module;
  }

  @Override
  public PermissionsUtil get() {
    return providePermissionsUtil(module);
  }

  public static AppModule_ProvidePermissionsUtilFactory create(AppModule module) {
    return new AppModule_ProvidePermissionsUtilFactory(module);
  }

  public static PermissionsUtil providePermissionsUtil(AppModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.providePermissionsUtil());
  }
}
