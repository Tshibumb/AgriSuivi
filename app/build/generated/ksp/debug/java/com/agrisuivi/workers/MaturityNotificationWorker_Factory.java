package com.agrisuivi.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.agrisuivi.data.repository.CultureRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class MaturityNotificationWorker_Factory {
  private final Provider<CultureRepository> repositoryProvider;

  public MaturityNotificationWorker_Factory(Provider<CultureRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public MaturityNotificationWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get());
  }

  public static MaturityNotificationWorker_Factory create(
      Provider<CultureRepository> repositoryProvider) {
    return new MaturityNotificationWorker_Factory(repositoryProvider);
  }

  public static MaturityNotificationWorker newInstance(Context context,
      WorkerParameters workerParams, CultureRepository repository) {
    return new MaturityNotificationWorker(context, workerParams, repository);
  }
}
