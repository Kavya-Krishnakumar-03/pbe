package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
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
public final class SignupService_Factory implements Factory<SignupService> {
  private final Provider<CognitoIdentityProviderClient> identityProviderClientProvider;

  private final Provider<AmazonDynamoDB> amazonDynamoDBProvider;

  public SignupService_Factory(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    this.identityProviderClientProvider = identityProviderClientProvider;
    this.amazonDynamoDBProvider = amazonDynamoDBProvider;
  }

  @Override
  public SignupService get() {
    return newInstance(identityProviderClientProvider.get(), amazonDynamoDBProvider.get());
  }

  public static SignupService_Factory create(
      Provider<CognitoIdentityProviderClient> identityProviderClientProvider,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    return new SignupService_Factory(identityProviderClientProvider, amazonDynamoDBProvider);
  }

  public static SignupService newInstance(CognitoIdentityProviderClient identityProviderClient,
      AmazonDynamoDB amazonDynamoDB) {
    return new SignupService(identityProviderClient, amazonDynamoDB);
  }
}
