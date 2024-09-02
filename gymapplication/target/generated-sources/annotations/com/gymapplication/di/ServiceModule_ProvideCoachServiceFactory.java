package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.gymapplication.service.CoachService;
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
public final class ServiceModule_ProvideCoachServiceFactory implements Factory<CoachService> {
  private final ServiceModule module;

  private final Provider<AmazonDynamoDB> dynamoDBProvider;

  public ServiceModule_ProvideCoachServiceFactory(ServiceModule module,
      Provider<AmazonDynamoDB> dynamoDBProvider) {
    this.module = module;
    this.dynamoDBProvider = dynamoDBProvider;
  }

  @Override
  public CoachService get() {
    return provideCoachService(module, dynamoDBProvider.get());
  }

  public static ServiceModule_ProvideCoachServiceFactory create(ServiceModule module,
      Provider<AmazonDynamoDB> dynamoDBProvider) {
    return new ServiceModule_ProvideCoachServiceFactory(module, dynamoDBProvider);
  }

  public static CoachService provideCoachService(ServiceModule instance, AmazonDynamoDB dynamoDB) {
    return Preconditions.checkNotNullFromProvides(instance.provideCoachService(dynamoDB));
  }
}
