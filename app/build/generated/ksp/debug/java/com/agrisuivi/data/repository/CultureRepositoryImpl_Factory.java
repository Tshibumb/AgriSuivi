package com.agrisuivi.data.repository;

import com.agrisuivi.data.remote.SupabaseDataSource;
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
public final class CultureRepositoryImpl_Factory implements Factory<CultureRepositoryImpl> {
  private final Provider<SupabaseDataSource> remoteProvider;

  public CultureRepositoryImpl_Factory(Provider<SupabaseDataSource> remoteProvider) {
    this.remoteProvider = remoteProvider;
  }

  @Override
  public CultureRepositoryImpl get() {
    return newInstance(remoteProvider.get());
  }

  public static CultureRepositoryImpl_Factory create(Provider<SupabaseDataSource> remoteProvider) {
    return new CultureRepositoryImpl_Factory(remoteProvider);
  }

  public static CultureRepositoryImpl newInstance(SupabaseDataSource remote) {
    return new CultureRepositoryImpl(remote);
  }
}
