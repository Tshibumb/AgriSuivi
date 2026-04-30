package com.agrisuivi;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class AgriSuiviApp_MembersInjector implements MembersInjector<AgriSuiviApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public AgriSuiviApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<AgriSuiviApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new AgriSuiviApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(AgriSuiviApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.agrisuivi.AgriSuiviApp.workerFactory")
  public static void injectWorkerFactory(AgriSuiviApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
