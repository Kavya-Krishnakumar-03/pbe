package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.regions.Region;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ApiModule {

    @Provides
    @Singleton
    public CognitoIdentityProviderClient provideCognitoClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(System.getenv("region")))
                .build();
    }

    @Provides
    @Singleton
    public AmazonDynamoDB provideDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(System.getenv("region"))
                .build();
    }
//
//    @Provides
//    @Singleton
//    public CognitoService provideCognitoService(CognitoIdentityProviderClient cognitoClient) {
//        return new CognitoService(cognitoClient);
//    }
}
