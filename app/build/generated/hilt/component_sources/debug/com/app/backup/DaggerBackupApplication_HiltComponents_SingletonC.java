package com.app.backup;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.repository.BackupRepository;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.data.restore.RestoreManager;
import com.app.backup.di.AppModule;
import com.app.backup.di.AppModule_ProvideAppDatabaseFactory;
import com.app.backup.di.AppModule_ProvideBackupManagerFactory;
import com.app.backup.di.AppModule_ProvideBackupRepositoryFactory;
import com.app.backup.di.AppModule_ProvideContactsRepositoryFactory;
import com.app.backup.di.AppModule_ProvideDriveServiceFactory;
import com.app.backup.di.AppModule_ProvideEncryptionUtilFactory;
import com.app.backup.di.AppModule_ProvidePermissionsUtilFactory;
import com.app.backup.di.AppModule_ProvideRestoreManagerFactory;
import com.app.backup.di.AppModule_ProvideSmsRepositoryFactory;
import com.app.backup.ui.activities.BackupActivity;
import com.app.backup.ui.activities.BackupActivity_MembersInjector;
import com.app.backup.ui.activities.BackupHistoryActivity;
import com.app.backup.ui.activities.DriveAccountActivity;
import com.app.backup.ui.activities.MainActivity;
import com.app.backup.ui.activities.MainActivity_MembersInjector;
import com.app.backup.ui.activities.RestoreActivity;
import com.app.backup.ui.activities.RestorePreviewActivity;
import com.app.backup.ui.activities.SettingsActivity;
import com.app.backup.ui.fragments.BackupFragment;
import com.app.backup.ui.fragments.DashboardFragment;
import com.app.backup.ui.fragments.HistoryFragment;
import com.app.backup.ui.fragments.RestoreFragment;
import com.app.backup.ui.fragments.SettingsFragment;
import com.app.backup.utils.PermissionsUtil;
import com.app.backup.utils.security.EncryptionUtil;
import com.app.backup.viewmodel.BackupViewModel;
import com.app.backup.viewmodel.BackupViewModel_HiltModules;
import com.app.backup.viewmodel.DashboardViewModel;
import com.app.backup.viewmodel.DashboardViewModel_HiltModules;
import com.app.backup.viewmodel.DriveViewModel;
import com.app.backup.viewmodel.DriveViewModel_HiltModules;
import com.app.backup.viewmodel.RestoreViewModel;
import com.app.backup.viewmodel.RestoreViewModel_HiltModules;
import com.app.backup.workers.BackupWorker;
import com.app.backup.workers.BackupWorker_AssistedFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerBackupApplication_HiltComponents_SingletonC {
  private DaggerBackupApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private AppModule appModule;

    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public BackupApplication_HiltComponents.SingletonC build() {
      if (appModule == null) {
        this.appModule = new AppModule();
      }
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(appModule, applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements BackupApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements BackupApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements BackupApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements BackupApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements BackupApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements BackupApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements BackupApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public BackupApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends BackupApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends BackupApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public void injectBackupFragment(BackupFragment backupFragment) {
    }

    @Override
    public void injectDashboardFragment(DashboardFragment dashboardFragment) {
    }

    @Override
    public void injectHistoryFragment(HistoryFragment historyFragment) {
    }

    @Override
    public void injectRestoreFragment(RestoreFragment restoreFragment) {
    }

    @Override
    public void injectSettingsFragment(SettingsFragment settingsFragment) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends BackupApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends BackupApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectBackupActivity(BackupActivity backupActivity) {
      injectBackupActivity2(backupActivity);
    }

    @Override
    public void injectBackupHistoryActivity(BackupHistoryActivity backupHistoryActivity) {
    }

    @Override
    public void injectDriveAccountActivity(DriveAccountActivity driveAccountActivity) {
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public void injectRestoreActivity(RestoreActivity restoreActivity) {
    }

    @Override
    public void injectRestorePreviewActivity(RestorePreviewActivity restorePreviewActivity) {
    }

    @Override
    public void injectSettingsActivity(SettingsActivity settingsActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>of(LazyClassKeyProvider.com_app_backup_viewmodel_BackupViewModel, BackupViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_app_backup_viewmodel_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_app_backup_viewmodel_DriveViewModel, DriveViewModel_HiltModules.KeyModule.provide(), LazyClassKeyProvider.com_app_backup_viewmodel_RestoreViewModel, RestoreViewModel_HiltModules.KeyModule.provide()));
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private BackupActivity injectBackupActivity2(BackupActivity instance) {
      BackupActivity_MembersInjector.injectDriveService(instance, singletonCImpl.provideDriveServiceProvider.get());
      return instance;
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPermissionsUtil(instance, singletonCImpl.providePermissionsUtilProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_app_backup_viewmodel_BackupViewModel = "com.app.backup.viewmodel.BackupViewModel";

      static String com_app_backup_viewmodel_DriveViewModel = "com.app.backup.viewmodel.DriveViewModel";

      static String com_app_backup_viewmodel_DashboardViewModel = "com.app.backup.viewmodel.DashboardViewModel";

      static String com_app_backup_viewmodel_RestoreViewModel = "com.app.backup.viewmodel.RestoreViewModel";

      @KeepFieldType
      BackupViewModel com_app_backup_viewmodel_BackupViewModel2;

      @KeepFieldType
      DriveViewModel com_app_backup_viewmodel_DriveViewModel2;

      @KeepFieldType
      DashboardViewModel com_app_backup_viewmodel_DashboardViewModel2;

      @KeepFieldType
      RestoreViewModel com_app_backup_viewmodel_RestoreViewModel2;
    }
  }

  private static final class ViewModelCImpl extends BackupApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<BackupViewModel> backupViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<DriveViewModel> driveViewModelProvider;

    private Provider<RestoreViewModel> restoreViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.backupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.driveViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.restoreViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>of(LazyClassKeyProvider.com_app_backup_viewmodel_BackupViewModel, ((Provider) backupViewModelProvider), LazyClassKeyProvider.com_app_backup_viewmodel_DashboardViewModel, ((Provider) dashboardViewModelProvider), LazyClassKeyProvider.com_app_backup_viewmodel_DriveViewModel, ((Provider) driveViewModelProvider), LazyClassKeyProvider.com_app_backup_viewmodel_RestoreViewModel, ((Provider) restoreViewModelProvider)));
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_app_backup_viewmodel_DashboardViewModel = "com.app.backup.viewmodel.DashboardViewModel";

      static String com_app_backup_viewmodel_BackupViewModel = "com.app.backup.viewmodel.BackupViewModel";

      static String com_app_backup_viewmodel_DriveViewModel = "com.app.backup.viewmodel.DriveViewModel";

      static String com_app_backup_viewmodel_RestoreViewModel = "com.app.backup.viewmodel.RestoreViewModel";

      @KeepFieldType
      DashboardViewModel com_app_backup_viewmodel_DashboardViewModel2;

      @KeepFieldType
      BackupViewModel com_app_backup_viewmodel_BackupViewModel2;

      @KeepFieldType
      DriveViewModel com_app_backup_viewmodel_DriveViewModel2;

      @KeepFieldType
      RestoreViewModel com_app_backup_viewmodel_RestoreViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.app.backup.viewmodel.BackupViewModel 
          return (T) new BackupViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideBackupManagerProvider.get(), singletonCImpl.provideBackupRepositoryProvider.get(), singletonCImpl.provideDriveServiceProvider.get());

          case 1: // com.app.backup.viewmodel.DashboardViewModel 
          return (T) new DashboardViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideBackupRepositoryProvider.get(), singletonCImpl.provideContactsRepositoryProvider.get(), singletonCImpl.provideSmsRepositoryProvider.get(), singletonCImpl.provideBackupManagerProvider.get());

          case 2: // com.app.backup.viewmodel.DriveViewModel 
          return (T) new DriveViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideDriveServiceProvider.get());

          case 3: // com.app.backup.viewmodel.RestoreViewModel 
          return (T) new RestoreViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideRestoreManagerProvider.get(), singletonCImpl.provideBackupRepositoryProvider.get(), singletonCImpl.provideDriveServiceProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends BackupApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends BackupApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends BackupApplication_HiltComponents.SingletonC {
    private final AppModule appModule;

    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<ContactsRepository> provideContactsRepositoryProvider;

    private Provider<SmsRepository> provideSmsRepositoryProvider;

    private Provider<EncryptionUtil> provideEncryptionUtilProvider;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<BackupManager> provideBackupManagerProvider;

    private Provider<BackupWorker_AssistedFactory> backupWorker_AssistedFactoryProvider;

    private Provider<DriveService> provideDriveServiceProvider;

    private Provider<PermissionsUtil> providePermissionsUtilProvider;

    private Provider<BackupRepository> provideBackupRepositoryProvider;

    private Provider<RestoreManager> provideRestoreManagerProvider;

    private SingletonCImpl(AppModule appModuleParam,
        ApplicationContextModule applicationContextModuleParam) {
      this.appModule = appModuleParam;
      this.applicationContextModule = applicationContextModuleParam;
      initialize(appModuleParam, applicationContextModuleParam);

    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return ImmutableMap.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>of("com.app.backup.workers.BackupWorker", ((Provider) backupWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final AppModule appModuleParam,
        final ApplicationContextModule applicationContextModuleParam) {
      this.provideContactsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ContactsRepository>(singletonCImpl, 2));
      this.provideSmsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SmsRepository>(singletonCImpl, 3));
      this.provideEncryptionUtilProvider = DoubleCheck.provider(new SwitchingProvider<EncryptionUtil>(singletonCImpl, 4));
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 5));
      this.provideBackupManagerProvider = DoubleCheck.provider(new SwitchingProvider<BackupManager>(singletonCImpl, 1));
      this.backupWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<BackupWorker_AssistedFactory>(singletonCImpl, 0));
      this.provideDriveServiceProvider = DoubleCheck.provider(new SwitchingProvider<DriveService>(singletonCImpl, 6));
      this.providePermissionsUtilProvider = DoubleCheck.provider(new SwitchingProvider<PermissionsUtil>(singletonCImpl, 7));
      this.provideBackupRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BackupRepository>(singletonCImpl, 8));
      this.provideRestoreManagerProvider = DoubleCheck.provider(new SwitchingProvider<RestoreManager>(singletonCImpl, 9));
    }

    @Override
    public void injectBackupApplication(BackupApplication backupApplication) {
      injectBackupApplication2(backupApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private BackupApplication injectBackupApplication2(BackupApplication instance) {
      BackupApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.app.backup.workers.BackupWorker_AssistedFactory 
          return (T) new BackupWorker_AssistedFactory() {
            @Override
            public BackupWorker create(Context context, WorkerParameters params) {
              return new BackupWorker(context, params, singletonCImpl.provideBackupManagerProvider.get());
            }
          };

          case 1: // com.app.backup.data.backup.BackupManager 
          return (T) AppModule_ProvideBackupManagerFactory.provideBackupManager(singletonCImpl.appModule, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideContactsRepositoryProvider.get(), singletonCImpl.provideSmsRepositoryProvider.get(), singletonCImpl.provideEncryptionUtilProvider.get(), singletonCImpl.provideAppDatabaseProvider.get());

          case 2: // com.app.backup.data.repository.ContactsRepository 
          return (T) AppModule_ProvideContactsRepositoryFactory.provideContactsRepository(singletonCImpl.appModule, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.app.backup.data.repository.SmsRepository 
          return (T) AppModule_ProvideSmsRepositoryFactory.provideSmsRepository(singletonCImpl.appModule, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.app.backup.utils.security.EncryptionUtil 
          return (T) AppModule_ProvideEncryptionUtilFactory.provideEncryptionUtil(singletonCImpl.appModule);

          case 5: // com.app.backup.data.database.AppDatabase 
          return (T) AppModule_ProvideAppDatabaseFactory.provideAppDatabase(singletonCImpl.appModule, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.app.backup.data.drive.DriveService 
          return (T) AppModule_ProvideDriveServiceFactory.provideDriveService(singletonCImpl.appModule, ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideAppDatabaseProvider.get());

          case 7: // com.app.backup.utils.PermissionsUtil 
          return (T) AppModule_ProvidePermissionsUtilFactory.providePermissionsUtil(singletonCImpl.appModule);

          case 8: // com.app.backup.data.repository.BackupRepository 
          return (T) AppModule_ProvideBackupRepositoryFactory.provideBackupRepository(singletonCImpl.appModule, singletonCImpl.provideAppDatabaseProvider.get());

          case 9: // com.app.backup.data.restore.RestoreManager 
          return (T) AppModule_ProvideRestoreManagerFactory.provideRestoreManager(singletonCImpl.appModule, singletonCImpl.provideContactsRepositoryProvider.get(), singletonCImpl.provideSmsRepositoryProvider.get(), singletonCImpl.provideEncryptionUtilProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
