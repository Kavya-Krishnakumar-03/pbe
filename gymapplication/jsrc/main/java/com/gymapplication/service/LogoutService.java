package com.gymapplication.service;

//import com.auth0.jwt.exceptions.JWTDecodeException;
//import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
//import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import javax.inject.Inject;
//
//public class LogoutService {
//
//    private final CognitoIdentityProviderClient identityProviderClient;
//
//    @Inject
//    public LogoutService(CognitoIdentityProviderClient identityProviderClient) {
//        this.identityProviderClient = identityProviderClient;
//    }
//
//    public String getUserPoolId(String userPoolName) {
//        System.out.println("Retrieving User Pool ID...");
//        try {
//            var request = ListUserPoolsRequest.builder().maxResults(50).build();
//            var response = identityProviderClient.listUserPools(request);
//            return response.userPools().stream()
//                    .filter(pool -> pool.name().equals(userPoolName))
//                    .map(UserPoolDescriptionType::id)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("User pool not found"));
//        } catch (CognitoIdentityProviderException e) {
//            System.err.println("Error listing user pools: " + e.awsErrorDetails().errorMessage());
//            throw new RuntimeException("Failed to retrieve user pool ID", e);
//        }
//    }
//
//    public void signOutUser(String authorizationToken) {
//        // Extract email from the JWT token
//        String email = extractEmailFromToken(authorizationToken);
//        if (email == null) {
//            throw new RuntimeException("Invalid token: unable to extract email");
//        }
//
//        String userPoolId = getUserPoolId("simple-booking-userpool"); // Replace with actual user pool name if needed
//
//        System.out.println("User Pool ID: " + userPoolId);
//        try {
//            AdminUserGlobalSignOutRequest request = AdminUserGlobalSignOutRequest.builder()
//                    .username(email)
//                    .userPoolId(userPoolId)
//                    .build();
//            identityProviderClient.adminUserGlobalSignOut(request);
//            System.out.println("User " + email + " has been logged out successfully.");
//        } catch (CognitoIdentityProviderException e) {
//            throw new RuntimeException("Error during logout: " + e.awsErrorDetails().errorMessage(), e);
//        }
//    }
//
//    private String extractEmailFromToken(String token) {
//        try {
//            DecodedJWT decodedJWT = JWT.decode(token);
//            return decodedJWT.getClaim("email").asString();
//        } catch (JWTDecodeException e) {
//            System.err.println("Error decoding JWT token: " + e.getMessage());
//            return null;
//        }
//    }
//}

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUserGlobalSignOutRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolDescriptionType;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class LogoutService {

    private final CognitoIdentityProviderClient identityProviderClient;
    private static final Set<String> blacklistedTokens = new HashSet<>();

    @Inject
    public LogoutService(CognitoIdentityProviderClient identityProviderClient) {
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

    public void signOutUser(String authorizationToken) {
        // Extract email from the JWT token
        String email = extractEmailFromToken(authorizationToken);
        if (email == null) {
            throw new RuntimeException("Invalid token: unable to extract email");
        }

        String userPoolId = getUserPoolId("team3-userpool"); // Replace with actual user pool name if needed

        System.out.println("User Pool ID: " + userPoolId);
        try {
            AdminUserGlobalSignOutRequest request = AdminUserGlobalSignOutRequest.builder()
                    .username(email)
                    .userPoolId(userPoolId)
                    .build();
            identityProviderClient.adminUserGlobalSignOut(request);
            System.out.println("User " + email + " has been logged out successfully.");

            // Blacklist the token after successful logout
            blacklistToken(authorizationToken);

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Error during logout: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private String extractEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (JWTDecodeException e) {
            System.err.println("Error decoding JWT token: " + e.getMessage());
            return null;
        }
    }

    private void blacklistToken(String token) {
        blacklistedTokens.add(token);
        System.out.println("Token has been blacklisted and is no longer valid.");
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
