package com.agrisuivi.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class SupabaseDataSource_Factory implements Factory<SupabaseDataSource> {
  private final Provider<SupabaseClient> supabaseProvider;

  public SupabaseDataSource_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public SupabaseDataSource get() {
    return newInstance(supabaseProvider.get());
  }

  public static SupabaseDataSource_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new SupabaseDataSource_Factory(supabaseProvider);
  }

  public static SupabaseDataSource newInstance(SupabaseClient supabase) {
    return new SupabaseDataSource(supabase);
  }
}
