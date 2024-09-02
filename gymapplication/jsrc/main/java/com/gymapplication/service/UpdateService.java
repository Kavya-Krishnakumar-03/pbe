package com.gymapplication.service;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UpdateService {

    private final AmazonDynamoDB amazonDynamoDB;
    private final String usersTableName;

    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9\\s]*$");

    @Inject
    public UpdateService(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.usersTableName = "Users"; // Set the table name here
    }

    public APIGatewayProxyResponseEvent updateUserDetails(String username, Map<String, String> updatedAttributes) {
        String validationError = validateInput(updatedAttributes);
        if (validationError != null) {
            return createErrorResponse(400, validationError);
        }

        try {

            Map<String, AttributeValue> updateExpressionValues = new HashMap<>();
            StringBuilder updateExpression = new StringBuilder("SET ");

            updatedAttributes.forEach((key, value) -> {
                updateExpression.append("#").append(key).append(" = :").append(key).append(", ");
                updateExpressionValues.put(":" + key, new AttributeValue().withS(value));
            });


            if (updateExpression.length() > 0) {
                updateExpression.setLength(updateExpression.length() - 2);
            }

            var updateItemRequest = new UpdateItemRequest()
                    .withTableName(usersTableName)
                    .addKeyEntry("email", new AttributeValue().withS(username))
                    .withUpdateExpression(updateExpression.toString())
                    .withExpressionAttributeNames(updatedAttributes.keySet().stream()
                            .collect(Collectors.toMap(k -> "#" + k, v -> v)))
                    .withExpressionAttributeValues(updateExpressionValues);

            UpdateItemResult updateItemResult = amazonDynamoDB.updateItem(updateItemRequest);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("{\"message\": \"User details updated successfully.\"}")
                    .withHeaders(getCorsHeaders());

        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse(500, "Failed to update user details in DynamoDB: " + e.getMessage());
        }
    }

    private String validateInput(Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return "No attributes provided.";
        }


        String name = attributes.get("name");
        if (name != null && (name.isEmpty() || NUMERIC_PATTERN.matcher(name).matches() || !ALPHABETIC_PATTERN.matcher(name).matches())) {
            return "Name is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }


        String target = attributes.get("target");
        if (target != null && (target.isEmpty() || NUMERIC_PATTERN.matcher(target).matches() || !ALPHABETIC_PATTERN.matcher(target).matches())) {
            return "Target is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }


        String preferableActivity = attributes.get("preferableActivity");
        if (preferableActivity != null && (preferableActivity.isEmpty() || NUMERIC_PATTERN.matcher(preferableActivity).matches() || !ALPHABETIC_PATTERN.matcher(preferableActivity).matches())) {
            return "Preferable activity is required and must contain at least one alphabetic character and cannot be purely numeric.";
        }

        return null;
    }

    private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody("{\"statusCode\":" + statusCode + ", \"message\":\"" + message + "\"}")
                .withHeaders(getCorsHeaders());
    }

    private Map<String, String> getCorsHeaders() {
        return Map.of(
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "POST, GET, OPTIONS",
                "Access-Control-Allow-Headers", "Content-Type, X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                "Accept-Version", "*"
        );
    }
}
