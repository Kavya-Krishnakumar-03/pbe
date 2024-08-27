package com.gymapplication.di;

import com.gymapplication.service.CognitoService;
import com.gymapplication.service.SignupService;
import com.gymapplication.service.SigninService;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public SignupService provideSignupService(CognitoIdentityProviderClient cognitoClient, AmazonDynamoDB dynamoDB) {
        return new SignupService(cognitoClient, dynamoDB);
    }

    @Provides
    @Singleton
    public SigninService provideSigninService(CognitoIdentityProviderClient cognitoClient) {
        return new SigninService(cognitoClient);
    }

}
