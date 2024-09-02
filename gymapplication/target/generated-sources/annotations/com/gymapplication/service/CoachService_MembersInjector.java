package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CoachService_MembersInjector implements MembersInjector<CoachService> {
  private final Provider<AmazonDynamoDB> dynamoDBProvider;

  public CoachService_MembersInjector(Provider<AmazonDynamoDB> dynamoDBProvider) {
    this.dynamoDBProvider = dynamoDBProvider;
  }

  public static MembersInjector<CoachService> create(Provider<AmazonDynamoDB> dynamoDBProvider) {
    return new CoachService_MembersInjector(dynamoDBProvider);
  }

  @Override
  public void injectMembers(CoachService instance) {
    injectDynamoDB(instance, dynamoDBProvider.get());
  }

  @InjectedFieldSignature("com.gymapplication.service.CoachService.dynamoDB")
  public static void injectDynamoDB(CoachService instance, AmazonDynamoDB dynamoDB) {
    instance.dynamoDB = dynamoDB;
  }
}
