package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "KotlinInternalInJava"
})
public final class ProfileUpdateService_Factory implements Factory<ProfileUpdateService> {
  private final Provider<AmazonDynamoDB> amazonDynamoDBProvider;

  public ProfileUpdateService_Factory(Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    this.amazonDynamoDBProvider = amazonDynamoDBProvider;
  }

  @Override
  public ProfileUpdateService get() {
    return newInstance(amazonDynamoDBProvider.get());
  }

  public static ProfileUpdateService_Factory create(
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    return new ProfileUpdateService_Factory(amazonDynamoDBProvider);
  }

  public static ProfileUpdateService newInstance(AmazonDynamoDB amazonDynamoDB) {
    return new ProfileUpdateService(amazonDynamoDB);
  }
}
