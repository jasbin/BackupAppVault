package com.app.backup.di;

import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.data.restore.RestoreManager;
import com.app.backup.utils.security.EncryptionUtil;
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
public final class AppModule_ProvideRestoreManagerFactory implements Factory<RestoreManager> {
  private final AppModule module;

  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<SmsRepository> smsRepositoryProvider;

  private final Provider<EncryptionUtil> encryptionUtilProvider;

  public AppModule_ProvideRestoreManagerFactory(AppModule module,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider) {
    this.module = module;
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.smsRepositoryProvider = smsRepositoryProvider;
    this.encryptionUtilProvider = encryptionUtilProvider;
  }

  @Override
  public RestoreManager get() {
    return provideRestoreManager(module, contactsRepositoryProvider.get(), smsRepositoryProvider.get(), encryptionUtilProvider.get());
  }

  public static AppModule_ProvideRestoreManagerFactory create(AppModule module,
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider) {
    return new AppModule_ProvideRestoreManagerFactory(module, contactsRepositoryProvider, smsRepositoryProvider, encryptionUtilProvider);
  }

  public static RestoreManager provideRestoreManager(AppModule instance,
      ContactsRepository contactsRepository, SmsRepository smsRepository,
      EncryptionUtil encryptionUtil) {
    return Preconditions.checkNotNullFromProvides(instance.provideRestoreManager(contactsRepository, smsRepository, encryptionUtil));
  }
}
