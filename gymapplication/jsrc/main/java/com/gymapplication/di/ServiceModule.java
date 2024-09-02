package com.gymapplication.di;

import com.gymapplication.service.CoachService;
import com.gymapplication.service.SignupService;
import com.gymapplication.service.SigninService;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.gymapplication.service.UpdateService;
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

    @Provides
    @Singleton
    public UpdateService provideUpdateService(AmazonDynamoDB amazonDynamoDB) {
        return new UpdateService(amazonDynamoDB);
    }

    @Provides
    @Singleton
    public CoachService provideCoachService(AmazonDynamoDB dynamoDB) {
        return new CoachService(dynamoDB);
    }

}
