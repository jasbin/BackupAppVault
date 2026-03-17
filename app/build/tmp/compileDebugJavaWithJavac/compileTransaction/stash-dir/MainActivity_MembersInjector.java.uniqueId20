package com.app.backup.ui.activities;

import com.app.backup.utils.PermissionsUtil;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PermissionsUtil> permissionsUtilProvider;

  public MainActivity_MembersInjector(Provider<PermissionsUtil> permissionsUtilProvider) {
    this.permissionsUtilProvider = permissionsUtilProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<PermissionsUtil> permissionsUtilProvider) {
    return new MainActivity_MembersInjector(permissionsUtilProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPermissionsUtil(instance, permissionsUtilProvider.get());
  }

  @InjectedFieldSignature("com.app.backup.ui.activities.MainActivity.permissionsUtil")
  public static void injectPermissionsUtil(MainActivity instance, PermissionsUtil permissionsUtil) {
    instance.permissionsUtil = permissionsUtil;
  }
}
