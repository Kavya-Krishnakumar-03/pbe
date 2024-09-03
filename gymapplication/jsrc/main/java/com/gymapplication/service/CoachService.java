//package com.gymapplication.service;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ScanRequest;
//import com.amazonaws.services.dynamodbv2.model.ScanResult;
//import javax.inject.Inject;
//import java.nio.ByteBuffer;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class CoachService {
//
//    @Inject
//    AmazonDynamoDB dynamoDB;
//
//    private final String coachesTable;
//
//    public CoachService(AmazonDynamoDB amazonDynamoDB) {
//        this.dynamoDB = amazonDynamoDB;
//        this.coachesTable = "coach";
//    }
//
//    public List<Map<String, Object>> getAvailableCoaches() {
//        System.out.println("getAvailableCoaches: Start");
//
//        if (dynamoDB == null) {
//            System.out.println("dynamoDB is null!");
//            throw new RuntimeException("dynamoDB is not initialized");
//        }
//
//        try {
//            ScanRequest scanRequest = new ScanRequest().withTableName(coachesTable);
//            System.out.println("ScanRequest created for table: " + coachesTable);
//
//            ScanResult result = dynamoDB.scan(scanRequest);
//            System.out.println("ScanResult obtained. Number of items: " + result.getCount());
//
//            return result.getItems().stream().map(item -> {
//                String profilePictureUrl = decodeBinaryToUrl(item.get("ProfilePictureUrl"));
//                List<String> clientReviews = item.getOrDefault("ClientReviews", new AttributeValue().withL(List.of()))
//                        .getL().stream()
//                        .map(AttributeValue::getS)
//                        .collect(Collectors.toList());
//
//                System.out.println("Mapping coach item: " + item.get("Name").getS());
//
//                return Map.of(
//                        "Name", item.get("Name") != null ? item.get("Name").getS() : "",
//                        "Summary", item.get("Summary") != null ? item.get("Summary").getS() : "",
//                        "Expertise", item.get("Expertise") != null ? item.get("Expertise").getS() : "",
//                        "Ratings", item.get("Ratings") != null ? item.get("Ratings").getN() : "0",
//                        "ProfilePictureUrl", profilePictureUrl,
//                        "ClientReviews", clientReviews
//                );
//            }).collect(Collectors.toList());
//        } catch (Exception e) {
//            System.out.println("Error during DynamoDB scan: " + e.getMessage());
//            throw e;
//        }
//    }
//
//
//    private String decodeBinaryToUrl(AttributeValue profilePictureBinary) {
//        if (profilePictureBinary == null || profilePictureBinary.getB() == null) {
//            System.out.println("ProfilePictureUrl is null or not found.");
//            return null;
//        }
//
//        ByteBuffer byteBuffer = profilePictureBinary.getB();
//        byte[] binaryData = new byte[byteBuffer.remaining()];
//        byteBuffer.get(binaryData);
//
//        String url = new String(binaryData, StandardCharsets.UTF_8);
//        System.out.println("ProfilePictureUrl decoded to URL: " + url);
//        return url;
//    }
//
//}
//

package com.gymapplication.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoachService {

    @Inject
    AmazonDynamoDB dynamoDB;

    private final String coachesTable;

    public CoachService(AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDB = amazonDynamoDB;
        this.coachesTable = "coach";
    }

    public List<Map<String, Object>> getAvailableCoaches() {
        System.out.println("getAvailableCoaches: Start");

        if (dynamoDB == null) {
            System.out.println("dynamoDB is null!");
            throw new RuntimeException("dynamoDB is not initialized");
        }

        try {
            ScanRequest scanRequest = new ScanRequest().withTableName(coachesTable);
            System.out.println("ScanRequest created for table: " + coachesTable);

            ScanResult result = dynamoDB.scan(scanRequest);
            System.out.println("ScanResult obtained. Number of items: " + result.getCount());

            return result.getItems().stream().map(item -> {
                String profilePictureUrl = decodeBinaryToUrl(item.get("ProfilePictureUrl"));
                List<String> clientReviews = item.getOrDefault("ClientReviews", new AttributeValue().withL(List.of()))
                        .getL().stream()
                        .map(AttributeValue::getS)
                        .collect(Collectors.toList());

                System.out.println("Mapping coach item: " + item.get("Name").getS());

                // Use the coach's email to generate the BookWorkoutUrl
                String email = item.get("Email").getS();
                String bookWorkoutUrl = generateBookWorkoutUrl(email);

                return Map.of(
                        "Name", item.get("Name") != null ? item.get("Name").getS() : "",
                        "Summary", item.get("Summary") != null ? item.get("Summary").getS() : "",
                        "Expertise", item.get("Expertise") != null ? item.get("Expertise").getS() : "",
                        "Ratings", item.get("Ratings") != null ? item.get("Ratings").getN() : "0",
                        "ProfilePictureUrl", profilePictureUrl,
                        "ClientReviews", clientReviews,
                        "BookWorkoutUrl", bookWorkoutUrl
                );
            }).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error during DynamoDB scan: " + e.getMessage());
            throw e;
        }
    }

    private String generateBookWorkoutUrl(String email) {
        String baseUrl = "https://gymapp.com/book-workout?coachEmail=";
        String bookWorkoutUrl = baseUrl + email;
        System.out.println("Generated BookWorkoutUrl: " + bookWorkoutUrl);
        return bookWorkoutUrl;
    }

    private String decodeBinaryToUrl(AttributeValue profilePictureBinary) {
        if (profilePictureBinary == null || profilePictureBinary.getB() == null) {
            System.out.println("ProfilePictureUrl is null or not found.");
            return null;
        }

        ByteBuffer byteBuffer = profilePictureBinary.getB();
        byte[] binaryData = new byte[byteBuffer.remaining()];
        byteBuffer.get(binaryData);

        String url = new String(binaryData, StandardCharsets.UTF_8);
        System.out.println("ProfilePictureUrl decoded to URL: " + url);
        return url;
    }
}

