package com.gymapplication.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
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

    public APIGatewayProxyResponseEvent signInUser(Map<String, String> credentials, String userPoolId, String clientId) {
        String validationError = validateInput(credentials);
        if (validationError != null) {
            return createErrorResponse(400, validationError);
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
                return createErrorResponse(500, "Authentication failed: no token received.");
            }

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("{\"id_token\":\"" + authResult.idToken() + "\"}");
        } catch (NotAuthorizedException e) {

            return createErrorResponse(401, "Incorrect username or password.");
        } catch (UserNotFoundException e) {
            return createErrorResponse(404, "User not found.");
        } catch (CognitoIdentityProviderException e) {
            return createErrorResponse(500, "Failed to sign in user: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody("{\"statusCode\":" + statusCode + ", \"message\":\"" + message + "\"}");
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






 