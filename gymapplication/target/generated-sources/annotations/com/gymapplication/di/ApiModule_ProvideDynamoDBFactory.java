package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ApiModule_ProvideDynamoDBFactory implements Factory<AmazonDynamoDB> {
  private final ApiModule module;

  public ApiModule_ProvideDynamoDBFactory(ApiModule module) {
    this.module = module;
  }

  @Override
  public AmazonDynamoDB get() {
    return provideDynamoDB(module);
  }

  public static ApiModule_ProvideDynamoDBFactory create(ApiModule module) {
    return new ApiModule_ProvideDynamoDBFactory(module);
  }

  public static AmazonDynamoDB provideDynamoDB(ApiModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideDynamoDB());
  }
}
