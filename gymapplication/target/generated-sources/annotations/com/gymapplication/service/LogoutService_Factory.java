package com.gymapplication.service;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

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
    "KotlinInternalInJava"
})
public final class LogoutService_Factory implements Factory<LogoutService> {
  private final Provider<CognitoIdentityProviderClient> identityProviderClientProvider;

  public LogoutService_Factory(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    this.identityProviderClientProvider = identityProviderClientProvider;
  }

  @Override
  public LogoutService get() {
    return newInstance(identityProviderClientProvider.get());
  }

  public static LogoutService_Factory create(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    return new LogoutService_Factory(identityProviderClientProvider);
  }

  public static LogoutService newInstance(CognitoIdentityProviderClient identityProviderClient) {
    return new LogoutService(identityProviderClient);
  }
}
