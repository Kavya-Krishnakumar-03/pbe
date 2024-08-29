package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;

public class SignupService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final CognitoIdentityProviderClient identityProviderClient;
    private final AmazonDynamoDB amazonDynamoDB;

    @Inject
    public SignupService(CognitoIdentityProviderClient identityProviderClient, AmazonDynamoDB amazonDynamoDB) {
        this.identityProviderClient = identityProviderClient;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public String signUpUser(Map<String, String> userDetails, String userPoolId) {
        // Validate input
        String validationError = validateInput(userDetails);
        if (validationError != null) {
            return validationError;
        }

        String role = determineUserRole(userDetails.get("email"));

        try {
            List<AttributeType> userAttributes = new ArrayList<>();
            userAttributes.add(AttributeType.builder().name("email").value(userDetails.get("email")).build());
//            userAttributes.add(AttributeType.builder().name("custom:role").value(role).build()); // Set the role attribute

            AdminCreateUserRequest createUserRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(userDetails.get("email"))
                    .userAttributes(userAttributes)
                    .temporaryPassword(userDetails.get("password"))
                    .messageAction(MessageActionType.SUPPRESS)
                    .build();

            identityProviderClient.adminCreateUser(createUserRequest);

            // Store user details in DynamoDB
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("email", new AttributeValue().withS(userDetails.get("email")));
            item.put("name", new AttributeValue().withS(userDetails.get("name")));
            item.put("target", new AttributeValue().withS(userDetails.get("target")));
            item.put("preferableActivity", new AttributeValue().withS(userDetails.get("preferableActivity")));
            item.put("role", new AttributeValue().withS(role)); // Store the role in DynamoDB

            amazonDynamoDB.putItem(new PutItemRequest()
                    .withTableName(System.getenv("tables_table"))
                    .withItem(item));

            System.out.println("User signed up successfully.");

            return null; // No error
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error signing up user: " + e.awsErrorDetails().errorMessage());
            return "Error signing up user: " + e.awsErrorDetails().errorMessage();
        }
    }
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    private String validateInput(Map<String, String> userDetails) {
//
        if (userDetails.get("email") == null || !EMAIL_PATTERN.matcher(userDetails.get("email")).matches()) {
            return "Invalid email format.";
        }

        // Validate password
        if (userDetails.get("password") == null || userDetails.get("password").length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.";
        }

        // Validate name
        String name = userDetails.get("name");
        if (name == null || name.isEmpty() || NUMERIC_PATTERN.matcher(name).matches() || !ALPHANUMERIC_PATTERN.matcher(name).matches()) {
            return "Name is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }

        // Validate target
        String target = userDetails.get("target");
        if (target == null || target.isEmpty() || NUMERIC_PATTERN.matcher(target).matches() || !ALPHANUMERIC_PATTERN.matcher(target).matches()) {
            return "Target is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }

        // Validate preferableActivity
        String preferableActivity = userDetails.get("preferableActivity");
        if (preferableActivity == null || preferableActivity.isEmpty() || NUMERIC_PATTERN.matcher(preferableActivity).matches() || !ALPHANUMERIC_PATTERN.matcher(preferableActivity).matches()) {
            return "Preferable activity is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }

        // Check for duplicate email (optional, can be handled by Cognito as well)
        if (isEmailAlreadyRegistered(userDetails.get("email"))) {
            return "Email is already registered.";
        }

        return null; // No validation errors
    }

    private String determineUserRole(String email) {
        if (isEmailInTable(email, System.getenv("coaches_table"))) {
            return "coach";
        } else if (isEmailInTable(email, System.getenv("admins_table"))) {
            return "admin";
        } else {
            return "client"; // Default role
        }
    }

    private boolean isEmailInTable(String email, String tableName) {
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("email", new AttributeValue().withS(email));

            Map<String, AttributeValue> item = amazonDynamoDB.getItem(tableName, key).getItem();

            return item != null && !item.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking email in " + tableName + " table: " + e.getMessage());
            return false; // In case of an error, assume the email is not in the table
        }
    }

    private boolean isEmailAlreadyRegistered(String email) {
        // Check in DynamoDB
        if (isEmailInDynamoDB(email)) {
            return true;
        }

        // Check in Cognito
        if (isEmailInCognito(email)) {
            return true;
        }

        return false; // Email is not registered
    }

    private boolean isEmailInDynamoDB(String email) {
        try {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("email", new AttributeValue().withS(email));

            Map<String, AttributeValue> item = amazonDynamoDB.getItem(System.getenv("tables_table"), key).getItem();

            return item != null && !item.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking email in DynamoDB: " + e.getMessage());
            return false; // In case of an error, assume the email is not in DynamoDB
        }
    }

    private boolean isEmailInCognito(String email) {
        try {
            ListUsersRequest request = ListUsersRequest.builder()
                    .userPoolId(System.getenv("user_pool_id"))
                    .filter("email = \"" + email + "\"")
                    .limit(1)
                    .build();

            ListUsersResponse response = identityProviderClient.listUsers(request);

            return !response.users().isEmpty();
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error checking email in Cognito: " + e.awsErrorDetails().errorMessage());
            return false; // In case of an error, assume the email is not in Cognito
        }
    }
}
