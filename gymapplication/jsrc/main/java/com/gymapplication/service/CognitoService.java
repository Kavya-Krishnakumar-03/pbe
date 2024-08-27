package com.gymapplication.service;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import javax.inject.Inject;

public class CognitoService {

    private final CognitoIdentityProviderClient identityProviderClient;

    @Inject
    public CognitoService(CognitoIdentityProviderClient identityProviderClient) {
        this.identityProviderClient = identityProviderClient;
    }

    public String getUserPoolId(String userPoolName) {
        System.out.println("Retrieving User Pool ID...");
        try {
            var request = ListUserPoolsRequest.builder().maxResults(50).build();
            var response = identityProviderClient.listUserPools(request);
            return response.userPools().stream()
                    .filter(pool -> pool.name().equals(userPoolName))
                    .map(UserPoolDescriptionType::id)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User pool not found"));
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error listing user pools: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to retrieve user pool ID", e);
        }
    }

    public String createAppClient(String userPoolId) {
        System.out.println("Creating App Client...");
        try {
            var result = identityProviderClient.createUserPoolClient(
                    CreateUserPoolClientRequest.builder()
                            .userPoolId(userPoolId)
                            .explicitAuthFlows(ExplicitAuthFlowsType.ALLOW_ADMIN_USER_PASSWORD_AUTH, ExplicitAuthFlowsType.ALLOW_REFRESH_TOKEN_AUTH)
                            .clientName("api_client")
                            .build());
            return result.userPoolClient().clientId();
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error creating app client: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to create app client", e);
        }
    }
}
