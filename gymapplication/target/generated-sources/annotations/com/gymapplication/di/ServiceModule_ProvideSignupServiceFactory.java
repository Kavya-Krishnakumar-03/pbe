package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.gymapplication.service.SignupService;
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
public final class ServiceModule_ProvideSignupServiceFactory implements Factory<SignupService> {
  private final ServiceModule module;

  private final Provider<CognitoIdentityProviderClient> cognitoClientProvider;

  private final Provider<AmazonDynamoDB> dynamoDBProvider;

  public ServiceModule_ProvideSignupServiceFactory(ServiceModule module,
      Provider<CognitoIdentityProviderClient> cognitoClientProvider,
      Provider<AmazonDynamoDB> dynamoDBProvider) {
    this.module = module;
    this.cognitoClientProvider = cognitoClientProvider;
    this.dynamoDBProvider = dynamoDBProvider;
  }

  @Override
  public SignupService get() {
    return provideSignupService(module, cognitoClientProvider.get(), dynamoDBProvider.get());
  }

  public static ServiceModule_ProvideSignupServiceFactory create(ServiceModule module,
      Provider<CognitoIdentityProviderClient> cognitoClientProvider,
      Provider<AmazonDynamoDB> dynamoDBProvider) {
    return new ServiceModule_ProvideSignupServiceFactory(module, cognitoClientProvider, dynamoDBProvider);
  }

  public static SignupService provideSignupService(ServiceModule instance,
      CognitoIdentityProviderClient cognitoClient, AmazonDynamoDB dynamoDB) {
    return Preconditions.checkNotNullFromProvides(instance.provideSignupService(cognitoClient, dynamoDB));
  }
}
