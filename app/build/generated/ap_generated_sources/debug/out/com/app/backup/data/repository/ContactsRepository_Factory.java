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
public final class ContactsRepository_Factory implements Factory<ContactsRepository> {
  private final Provider<Context> contextProvider;

  public ContactsRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ContactsRepository get() {
    return newInstance(contextProvider.get());
  }

  public static ContactsRepository_Factory create(Provider<Context> contextProvider) {
    return new ContactsRepository_Factory(contextProvider);
  }

  public static ContactsRepository newInstance(Context context) {
    return new ContactsRepository(context);
  }
}
