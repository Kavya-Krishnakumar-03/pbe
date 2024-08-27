package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ProfileUpdateService {

    private final AmazonDynamoDB amazonDynamoDB;

    @Inject
    public ProfileUpdateService(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public String updateUserProfile(String email, String name, String target, String preferableActivity) {
        try {
            // Update DynamoDB
            Map<String, AttributeValue> attributeValues = new HashMap<>();
            if (name != null && !name.isEmpty()) {
                attributeValues.put(":name", new AttributeValue().withS(name));
            }
            if (target != null && !target.isEmpty()) {
                attributeValues.put(":target", new AttributeValue().withS(target));
            }
            if (preferableActivity != null && !preferableActivity.isEmpty()) {
                attributeValues.put(":preferableActivity", new AttributeValue().withS(preferableActivity));
            }

            if (!attributeValues.isEmpty()) {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("email", new AttributeValue().withS(email));

                String updateExpression = buildUpdateExpression(attributeValues);
                Map<String, String> expressionAttributeNames = buildExpressionAttributeNames();

                UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                        .withTableName(System.getenv("tables_table"))
                        .withKey(key)
                        .withUpdateExpression(updateExpression)
                        .withExpressionAttributeValues(attributeValues)
                        .withExpressionAttributeNames(expressionAttributeNames);

                UpdateItemResult result = amazonDynamoDB.updateItem(updateItemRequest);
            }

            return "User profile updated successfully.";
        } catch (Exception e) {
            System.err.println("Error updating user in DynamoDB: " + e.getMessage());
            return "Error updating user profile in DynamoDB: " + e.getMessage();
        }
    }

    private String buildUpdateExpression(Map<String, AttributeValue> attributeValues) {
        StringBuilder updateExpression = new StringBuilder("set ");
        for (String key : attributeValues.keySet()) {
            String attributeName = key.substring(1);

            // Create placeholder for reserved keywords
            if ("name".equals(attributeName)) {
                updateExpression.append("#name = ").append(key).append(", ");
            } else {
                updateExpression.append(attributeName).append(" = ").append(key).append(", ");
            }
        }

        updateExpression.setLength(updateExpression.length() - 2); // Remove trailing comma and space
        return updateExpression.toString();
    }

    private Map<String, String> buildExpressionAttributeNames() {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#name", "name");
        return expressionAttributeNames;
    }
}