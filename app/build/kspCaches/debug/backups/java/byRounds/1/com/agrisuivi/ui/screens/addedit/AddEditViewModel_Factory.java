package com.agrisuivi.ui.screens.addedit;

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
public final class AddEditViewModel_Factory implements Factory<AddEditViewModel> {
  private final Provider<CultureRepository> repositoryProvider;

  private final Provider<Context> contextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditViewModel_Factory(Provider<CultureRepository> repositoryProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.contextProvider = contextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditViewModel get() {
    return newInstance(repositoryProvider.get(), contextProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditViewModel_Factory create(Provider<CultureRepository> repositoryProvider,
      Provider<Context> contextProvider, Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditViewModel_Factory(repositoryProvider, contextProvider, savedStateHandleProvider);
  }

  public static AddEditViewModel newInstance(CultureRepository repository, Context context,
      SavedStateHandle savedStateHandle) {
    return new AddEditViewModel(repository, context, savedStateHandle);
  }
}
