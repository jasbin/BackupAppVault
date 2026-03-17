package com.app.backup.data.restore;

import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;
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
public final class RestoreManager_Factory implements Factory<RestoreManager> {
  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<SmsRepository> smsRepositoryProvider;

  private final Provider<EncryptionUtil> encryptionUtilProvider;

  public RestoreManager_Factory(Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider) {
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.smsRepositoryProvider = smsRepositoryProvider;
    this.encryptionUtilProvider = encryptionUtilProvider;
  }

  @Override
  public RestoreManager get() {
    return newInstance(contactsRepositoryProvider.get(), smsRepositoryProvider.get(), encryptionUtilProvider.get());
  }

  public static RestoreManager_Factory create(
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<SmsRepository> smsRepositoryProvider,
      Provider<EncryptionUtil> encryptionUtilProvider) {
    return new RestoreManager_Factory(contactsRepositoryProvider, smsRepositoryProvider, encryptionUtilProvider);
  }

  public static RestoreManager newInstance(ContactsRepository contactsRepository,
      SmsRepository smsRepository, EncryptionUtil encryptionUtil) {
    return new RestoreManager(contactsRepository, smsRepository, encryptionUtil);
  }
}
