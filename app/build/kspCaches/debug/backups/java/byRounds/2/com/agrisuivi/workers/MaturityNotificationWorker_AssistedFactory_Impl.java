package com.agrisuivi.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MaturityNotificationWorker_AssistedFactory_Impl implements MaturityNotificationWorker_AssistedFactory {
  private final MaturityNotificationWorker_Factory delegateFactory;

  MaturityNotificationWorker_AssistedFactory_Impl(
      MaturityNotificationWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MaturityNotificationWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<MaturityNotificationWorker_AssistedFactory> create(
      MaturityNotificationWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MaturityNotificationWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<MaturityNotificationWorker_AssistedFactory> createFactoryProvider(
      MaturityNotificationWorker_Factory delegateFactory) {
    return InstanceFactory.create(new MaturityNotificationWorker_AssistedFactory_Impl(delegateFactory));
  }
}
