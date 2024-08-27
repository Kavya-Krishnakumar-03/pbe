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
public final class CognitoService_Factory implements Factory<CognitoService> {
  private final Provider<CognitoIdentityProviderClient> identityProviderClientProvider;

  public CognitoService_Factory(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    this.identityProviderClientProvider = identityProviderClientProvider;
  }

  @Override
  public CognitoService get() {
    return newInstance(identityProviderClientProvider.get());
  }

  public static CognitoService_Factory create(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    return new CognitoService_Factory(identityProviderClientProvider);
  }

  public static CognitoService newInstance(CognitoIdentityProviderClient identityProviderClient) {
    return new CognitoService(identityProviderClient);
  }
}
