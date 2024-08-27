package com.gymapplication.di;

import com.gymapplication.service.SigninService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class ServiceModule_ProvideSigninServiceFactory implements Factory<SigninService> {
  private final ServiceModule module;

  private final Provider<CognitoIdentityProviderClient> cognitoClientProvider;

  public ServiceModule_ProvideSigninServiceFactory(ServiceModule module,
      Provider<CognitoIdentityProviderClient> cognitoClientProvider) {
    this.module = module;
    this.cognitoClientProvider = cognitoClientProvider;
  }

  @Override
  public SigninService get() {
    return provideSigninService(module, cognitoClientProvider.get());
  }

  public static ServiceModule_ProvideSigninServiceFactory create(ServiceModule module,
      Provider<CognitoIdentityProviderClient> cognitoClientProvider) {
    return new ServiceModule_ProvideSigninServiceFactory(module, cognitoClientProvider);
  }

  public static SigninService provideSigninService(ServiceModule instance,
      CognitoIdentityProviderClient cognitoClient) {
    return Preconditions.checkNotNullFromProvides(instance.provideSigninService(cognitoClient));
  }
}
