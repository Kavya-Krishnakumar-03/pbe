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
public final class UpdateService_Factory implements Factory<UpdateService> {
  private final Provider<AmazonDynamoDB> amazonDynamoDBProvider;

  public UpdateService_Factory(Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    this.amazonDynamoDBProvider = amazonDynamoDBProvider;
  }

  @Override
  public UpdateService get() {
    return newInstance(amazonDynamoDBProvider.get());
  }

  public static UpdateService_Factory create(Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    return new UpdateService_Factory(amazonDynamoDBProvider);
  }

  public static UpdateService newInstance(AmazonDynamoDB amazonDynamoDB) {
    return new UpdateService(amazonDynamoDB);
  }
}
