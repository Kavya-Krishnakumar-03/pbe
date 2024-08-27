package com.gymapplication.service;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.inject.Inject;
import java.util.Map;

public class SigninService {

    private final CognitoIdentityProviderClient identityProviderClient;

    @Inject
    public SigninService(CognitoIdentityProviderClient identityProviderClient) {
        this.identityProviderClient = identityProviderClient;
    }

    public String signInUser(Map<String, String> credentials, String userPoolId, String clientId) {
        String validationError = validateInput(credentials);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }

        try {
            var authRequest = AdminInitiateAuthRequest.builder()
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .authParameters(Map.of(
                            "USERNAME", credentials.get("email"),
                            "PASSWORD", credentials.get("password")
                    ))
                    .userPoolId(userPoolId)
                    .clientId(clientId)
                    .build();

            AdminInitiateAuthResponse authResponse = identityProviderClient.adminInitiateAuth(authRequest);
            var authResult = authResponse.authenticationResult();

            if (ChallengeNameType.NEW_PASSWORD_REQUIRED.equals(authResponse.challengeName())) {
                var challengeResponse = identityProviderClient.adminRespondToAuthChallenge(
                        AdminRespondToAuthChallengeRequest.builder()
                                .userPoolId(userPoolId)
                                .clientId(clientId)
                                .session(authResponse.session())
                                .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                .challengeResponses(Map.of(
                                        "NEW_PASSWORD", credentials.get("password"),
                                        "USERNAME", credentials.get("email")
                                ))
                                .build());
                authResult = challengeResponse.authenticationResult();
            }

            if (authResult == null || authResult.idToken() == null || authResult.idToken().isEmpty()) {
                throw new RuntimeException("Authentication failed: no token received.");
            }

            return authResult.idToken();
        } catch (NotAuthorizedException e) {
            throw new RuntimeException("Incorrect username or password.");
        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Failed to sign in user: " + e.getMessage(), e);
        }
    }

    private String validateInput(Map<String, String> credentials) {
        if (credentials.get("email") == null || credentials.get("email").isEmpty()) {
            return "Email is required.";
        }
        if (credentials.get("password") == null || credentials.get("password").isEmpty()) {
            return "Password is required.";
        }
        return null;
    }
}

 