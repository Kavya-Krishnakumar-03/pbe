package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.gymapplication.service.UpdateService;
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
public final class ServiceModule_ProvideUpdateServiceFactory implements Factory<UpdateService> {
  private final ServiceModule module;

  private final Provider<AmazonDynamoDB> amazonDynamoDBProvider;

  public ServiceModule_ProvideUpdateServiceFactory(ServiceModule module,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    this.module = module;
    this.amazonDynamoDBProvider = amazonDynamoDBProvider;
  }

  @Override
  public UpdateService get() {
    return provideUpdateService(module, amazonDynamoDBProvider.get());
  }

  public static ServiceModule_ProvideUpdateServiceFactory create(ServiceModule module,
      Provider<AmazonDynamoDB> amazonDynamoDBProvider) {
    return new ServiceModule_ProvideUpdateServiceFactory(module, amazonDynamoDBProvider);
  }

  public static UpdateService provideUpdateService(ServiceModule instance,
      AmazonDynamoDB amazonDynamoDB) {
    return Preconditions.checkNotNullFromProvides(instance.provideUpdateService(amazonDynamoDB));
  }
}
