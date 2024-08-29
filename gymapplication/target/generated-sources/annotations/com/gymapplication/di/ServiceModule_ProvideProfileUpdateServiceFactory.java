package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.gymapplication.service.ProfileUpdateService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
    "KotlinInternalInJava"
})
public final class ServiceModule_ProvideProfileUpdateServiceFactory implements Factory<ProfileUpdateService> {
  private final ServiceModule module;

  private final Provider<AmazonDynamoDB> amazonDynamoDBProvider;

  public ServiceModule_ProvideProfileUpdateServiceFactory(ServiceModule module,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    this.module = module;
    this.amazonDynamoDBProvider = amazonDynamoDBProvider;
  }

  @Override
  public ProfileUpdateService get() {
    return provideProfileUpdateService(module, amazonDynamoDBProvider.get());
  }

  public static ServiceModule_ProvideProfileUpdateServiceFactory create(ServiceModule module,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    return new ServiceModule_ProvideProfileUpdateServiceFactory(module, amazonDynamoDBProvider);
  }

  public static ProfileUpdateService provideProfileUpdateService(ServiceModule instance,
      AmazonDynamoDB amazonDynamoDB) {
    return Preconditions.checkNotNullFromProvides(instance.provideProfileUpdateService(amazonDynamoDB));
  }
}
