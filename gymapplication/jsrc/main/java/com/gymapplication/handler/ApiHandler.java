//package com.gymapplication.handler;
//
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gymapplication.service.SignupService;
//import com.gymapplication.service.SigninService;
//import com.gymapplication.service.CognitoService;
//import com.gymapplication.di.DaggerApiComponent;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.gymapplication.service.UpdateService;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
//import com.syndicate.deployment.annotations.lambda.LambdaHandler;
//import com.syndicate.deployment.model.DeploymentRuntime;
//import com.syndicate.deployment.model.RetentionSetting;
//import org.apache.http.HttpStatus;
//import javax.inject.Inject;
//import java.io.IOException;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@LambdaHandler(lambdaName = "api_handler",
//        roleName = "api_handler-role",
//        runtime = DeploymentRuntime.JAVA17,
//        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
//)
//@EnvironmentVariables(value = {
//        @EnvironmentVariable(key = "region", value = "${region}"),
//        @EnvironmentVariable(key = "tables_table", value = "${tables_table}"),
//        @EnvironmentVariable(key = "booking_userpool", value = "${booking_userpool}"),
//        @EnvironmentVariable(key = "coaches_table", value = "${coaches_table}"),
//        @EnvironmentVariable(key = "admins_table", value = "${admins_table}")
//})
//public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    @Inject
//    SignupService signupService;
//
//    @Inject
//    SigninService signinService;
//
//    @Inject
//    CognitoService cognitoService;
//
//    @Inject
//    UpdateService updateService;
//
//
//    public ApiHandler() {
//        DaggerApiComponent.create().inject(this);
//    }
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
//        System.out.println("API request received: " + requestEvent);
//
//        if (requestEvent.getBody() == null || requestEvent.getBody().isEmpty()) {
//            return response(HttpStatus.SC_BAD_REQUEST, "Invalid request body");
//        }
//
//        String userPoolName = System.getenv("booking_userpool");
//        String userPoolId = cognitoService.getUserPoolId(userPoolName);
//        if (userPoolId == null || userPoolId.isEmpty()) {
//            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve user pool ID");
//        }
//
//        switch (requestEvent.getPath()) {
//            case "/signup":
//                return handleSignup(requestEvent.getBody(), userPoolId);
//            case "/signin":
//                return handleSignin(requestEvent.getBody(), userPoolId);
//            case "/update":
//                String authToken = requestEvent.getHeaders().get("Authorization");
//                return handleUpdate(requestEvent.getBody(), authToken);
//            default:
//                return response(HttpStatus.SC_NOT_FOUND, "Endpoint not found");
//        }
//    }
//
//    private APIGatewayProxyResponseEvent handleSignup(String bodyJson, String userPoolId) {
//        Map<String, String> bodyMap = parseBodyJson(bodyJson);
//        String error = signupService.signUpUser(bodyMap, userPoolId);
//        System.out.println("Error: " + error);
//        if (error != null) {
//            return response(HttpStatus.SC_BAD_REQUEST, error);
//        }
//        return response(HttpStatus.SC_OK, "User created successfully");
//    }
//
//    private APIGatewayProxyResponseEvent handleSignin(String bodyJson, String userPoolId) {
//        Map<String, String> bodyMap = parseBodyJson(bodyJson);
//        String clientId = cognitoService.createAppClient(userPoolId);
//        if (clientId == null) {
//            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Unable to create app client");
//        }
//        return signinService.signInUser(bodyMap, userPoolId, clientId);
//    }
//
//    private APIGatewayProxyResponseEvent handleUpdate(String bodyJson, String authToken) {
//        // Extract email from the JWT token (assuming Cognito JWT)
//        System.out.println("calling handleUpdate....");
//        System.out.println("Auth token: " + authToken);
//        System.out.println("Body: " + bodyJson);
//        String email = extractEmailFromAuthToken(authToken);
//        System.out.println("Email: " + email);
//        if (email == null || email.isEmpty()) {
//            return response(HttpStatus.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");
//        }
//
//
//        Map<String, String> bodyMap = parseBodyJson(bodyJson);
//
//
//        Map<String, String> updateAttributes = bodyMap.entrySet()
//                .stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//
//        return updateService.updateUserDetails(email, updateAttributes);
//    }
//
//
//    private String extractEmailFromAuthToken(String authToken) {
//        if (authToken == null || !authToken.startsWith("Bearer ")) {
//            return null;
//        }
//
//        String token = authToken.substring(7); // Remove "Bearer " prefix
//
//        try {
//
//            DecodedJWT jwt = JWT.decode(token);
//            return jwt.getClaim("email").asString(); // Ensure "email" is the correct claim name
//        } catch (JWTDecodeException e) {
//
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    private Map<String, String> parseBodyJson(String bodyJson) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(bodyJson, new TypeReference<Map<String, String>>() {});
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse request body JSON", e);
//        }
//    }
//
//    private APIGatewayProxyResponseEvent response(int statusCode, String message) {
//        return new APIGatewayProxyResponseEvent()
//                .withStatusCode(statusCode)
//                .withBody("{\"message\":\"" + message + "\"}")
//                .withHeaders(getCorsHeaders());
//    }
//
//    private Map<String, String> getCorsHeaders() {
//        return Map.of(
//                "Access-Control-Allow-Origin", "*",
//                "Access-Control-Allow-Methods", "POST, GET, OPTIONS",
//                "Access-Control-Allow-Headers", "Content-Type, X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
//                "Accept-Version", "*"
//        );
//    }
//}

package com.gymapplication.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymapplication.service.*;
import com.gymapplication.di.DaggerApiComponent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import org.apache.http.HttpStatus;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        runtime = DeploymentRuntime.JAVA17,
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
        @EnvironmentVariable(key = "region", value = "${region}"),
        @EnvironmentVariable(key = "tables_table", value = "${tables_table}"),
        @EnvironmentVariable(key = "booking_userpool", value = "${booking_userpool}"),
        @EnvironmentVariable(key = "coaches_table", value = "${coaches_table}"),
        @EnvironmentVariable(key = "admins_table", value = "${admins_table}")
})
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    SignupService signupService;

    @Inject
    SigninService signinService;

    @Inject
    CognitoService cognitoService;

    @Inject
    UpdateService updateService;

    @Inject
    CoachService coachService;

    public ApiHandler() {
        DaggerApiComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        System.out.println("API request received: " + requestEvent);

        if (requestEvent.getPath() == null) {
            return response(HttpStatus.SC_BAD_REQUEST, "Invalid request path");
        }

        String userPoolName = System.getenv("booking_userpool");
        String userPoolId = cognitoService.getUserPoolId(userPoolName);
        if (userPoolId == null || userPoolId.isEmpty()) {
            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve user pool ID");
        }

        try {
            switch (requestEvent.getPath()) {
                case "/signup":
                    return handleSignup(requestEvent.getBody(), userPoolId);
                case "/signin":
                    return handleSignin(requestEvent.getBody(), userPoolId);
                case "/update":
                    String authToken = requestEvent.getHeaders().get("Authorization");
                    return handleUpdate(requestEvent.getBody(), authToken);
                case "/coaches":
                    return handleGetAvailableCoaches();
                default:
                    return response(HttpStatus.SC_NOT_FOUND, "Endpoint not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleSignup(String bodyJson, String userPoolId) {
        if (bodyJson == null || bodyJson.isEmpty()) {
            return response(HttpStatus.SC_BAD_REQUEST, "Signup request body is empty");
        }
        Map<String, String> bodyMap = parseBodyJson(bodyJson);
        String error = signupService.signUpUser(bodyMap, userPoolId);
        if (error != null) {
            return response(HttpStatus.SC_BAD_REQUEST, error);
        }
        return response(HttpStatus.SC_OK, "User created successfully");
    }

    private APIGatewayProxyResponseEvent handleSignin(String bodyJson, String userPoolId) {
        if (bodyJson == null || bodyJson.isEmpty()) {
            return response(HttpStatus.SC_BAD_REQUEST, "Signin request body is empty");
        }
        Map<String, String> bodyMap = parseBodyJson(bodyJson);
        String clientId = cognitoService.createAppClient(userPoolId);
        if (clientId == null) {
            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Unable to create app client");
        }
        return signinService.signInUser(bodyMap, userPoolId, clientId);
    }

    private APIGatewayProxyResponseEvent handleUpdate(String bodyJson, String authToken) {
        if (bodyJson == null || bodyJson.isEmpty()) {
            return response(HttpStatus.SC_BAD_REQUEST, "Update request body is empty");
        }
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            return response(HttpStatus.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");
        }

        String email = extractEmailFromAuthToken(authToken);
        if (email == null || email.isEmpty()) {
            return response(HttpStatus.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");
        }

        Map<String, String> bodyMap = parseBodyJson(bodyJson);
        Map<String, String> updateAttributes = bodyMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return updateService.updateUserDetails(email, updateAttributes);
    }

    private APIGatewayProxyResponseEvent handleGetAvailableCoaches() {
        System.out.println("handleGetAvailableCoaches: Start");

        try {
            List<Map<String, Object>> coaches = coachService.getAvailableCoaches();
            System.out.println("Number of coaches found: " + coaches.size());

            String responseBody = new ObjectMapper().writeValueAsString(coaches);
            System.out.println("ResponseBody serialized successfully.");

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_OK)
                    .withBody(responseBody)
                    .withHeaders(getCorsHeaders());
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException occurred: " + e.getMessage());
            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Failed to serialize coaches data");
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return response(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
        }
    }



    private String extractEmailFromAuthToken(String authToken) {
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            return null;
        }

        String token = authToken.substring(7); // Remove "Bearer " prefix

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("email").asString(); // Ensure "email" is the correct claim name
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, String> parseBodyJson(String bodyJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(bodyJson, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse request body JSON", e);
        }
    }

    private APIGatewayProxyResponseEvent response(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody("{\"message\":\"" + message + "\"}")
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
