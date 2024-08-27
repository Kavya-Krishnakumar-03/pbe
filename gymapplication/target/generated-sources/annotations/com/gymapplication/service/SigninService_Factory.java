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
public final class SigninService_Factory implements Factory<SigninService> {
  private final Provider<CognitoIdentityProviderClient> identityProviderClientProvider;

  public SigninService_Factory(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    this.identityProviderClientProvider = identityProviderClientProvider;
  }

  @Override
  public SigninService get() {
    return newInstance(identityProviderClientProvider.get());
  }

  public static SigninService_Factory create(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider) {
    return new SigninService_Factory(identityProviderClientProvider);
  }

  public static SigninService newInstance(CognitoIdentityProviderClient identityProviderClient) {
    return new SigninService(identityProviderClient);
  }
}
