package com.agrisuivi.ui.screens.detail;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.agrisuivi.data.repository.CultureRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<CultureRepository> repositoryProvider;

  private final Provider<Context> contextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public DetailViewModel_Factory(Provider<CultureRepository> repositoryProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.contextProvider = contextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(repositoryProvider.get(), contextProvider.get(), savedStateHandleProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<CultureRepository> repositoryProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new DetailViewModel_Factory(repositoryProvider, contextProvider, savedStateHandleProvider);
  }

  public static DetailViewModel newInstance(CultureRepository repository, Context context,
      SavedStateHandle savedStateHandle) {
    return new DetailViewModel(repository, context, savedStateHandle);
  }
}
