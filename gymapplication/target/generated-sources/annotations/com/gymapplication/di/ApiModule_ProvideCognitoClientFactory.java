package com.gymapplication.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

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
    "KotlinInternalInJava"
})
public final class ApiModule_ProvideCognitoClientFactory implements Factory<CognitoIdentityProviderClient> {
  private final ApiModule module;

  public ApiModule_ProvideCognitoClientFactory(ApiModule module) {
    this.module = module;
  }

  @Override
  public CognitoIdentityProviderClient get() {
    return provideCognitoClient(module);
  }

  public static ApiModule_ProvideCognitoClientFactory create(ApiModule module) {
    return new ApiModule_ProvideCognitoClientFactory(module);
  }

  public static CognitoIdentityProviderClient provideCognitoClient(ApiModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideCognitoClient());
  }
}
