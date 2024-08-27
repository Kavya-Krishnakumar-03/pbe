//package com.gymapplication.handler;
//
//import com.gymapplication.di.DaggerApiComponent;
//import com.gymapplication.service.ProfileUpdateService;
//import com.gymapplication.service.SignupService;
//import com.gymapplication.service.SigninService;
//import com.gymapplication.service.CognitoService;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
//import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
//import com.syndicate.deployment.annotations.lambda.LambdaHandler;
//import com.syndicate.deployment.model.DeploymentRuntime;
//import com.syndicate.deployment.model.RetentionSetting;
//import javax.inject.Inject;
//import java.util.Map;
//
//@LambdaHandler(lambdaName = "api_handler",
//        roleName = "api_handler-role",
//        runtime = DeploymentRuntime.JAVA17,
//        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
//)
//@EnvironmentVariables(value = {
//        @EnvironmentVariable(key = "region", value = "${region}"),
//        @EnvironmentVariable(key = "tables_table", value = "${tables_table}"),
//        @EnvironmentVariable(key = "booking_userpool", value = "${booking_userpool}")
//})
//public class ApiHandler implements RequestHandler<ApiHandler.APIRequest, APIGatewayV2HTTPResponse> {
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
//    ProfileUpdateService profileUpdateService;
//
//    public ApiHandler() {
//        DaggerApiComponent.create().inject(this);
//    }
//
//    @Override
//    public APIGatewayV2HTTPResponse handleRequest(APIRequest requestEvent, Context context) {
//        System.out.println("API request received: " + requestEvent);
//
//        if (requestEvent.body_json() == null || requestEvent.body_json().isEmpty()) {
//            return createErrorResponse(400, "Invalid request body");
//        }
//
//        String userPoolName = System.getenv("booking_userpool");
//        String userPoolId = cognitoService.getUserPoolId(userPoolName);
//        if (userPoolId == null || userPoolId.isEmpty()) {
//            return createErrorResponse(500, "Unable to retrieve user pool ID");
//        }
//
//        switch (requestEvent.path()) {
//            case "/signup":
//                return handleSignup(requestEvent.body_json(), userPoolId);
//            case "/signin":
//                return handleSignin(requestEvent.body_json(), userPoolId);
//            case "/updateProfile":
//                return handleProfileUpdate(requestEvent.body_json());
//            default:
//                return createErrorResponse(404, "Endpoint not found");
//        }
//    }
//
//    private APIGatewayV2HTTPResponse handleSignup(Map<String, String> bodyJson, String userPoolId) {
//        try {
//            String error = signupService.signUpUser(bodyJson, userPoolId);
//            if (error != null) {
//                return createErrorResponse(400, error);
//            }
//            return createSuccessResponse(200, "{\"message\":\"User created successfully\"}");
//        } catch (Exception e) {
//            return createErrorResponse(500, "Error processing signup: " + e.getMessage());
//        }
//    }
//
//    private APIGatewayV2HTTPResponse handleSignin(Map<String, String> bodyJson, String userPoolId) {
//        try {
//            String clientId = cognitoService.createAppClient(userPoolId);
//            if (clientId == null) {
//                return createErrorResponse(500, "Unable to create app client");
//            }
//            String idToken = signinService.signInUser(bodyJson, userPoolId, clientId);
//            if (idToken == null) {
//                return createErrorResponse(400, "Incorrect username or password.");
//            }
//            return createSuccessResponse(200, "{\"id_token\":\"" + idToken + "\"}");
//        } catch (Exception e) {
//            return createErrorResponse(500, "Error processing signin: " + e.getMessage());
//        }
//    }
//
//    private APIGatewayV2HTTPResponse handleProfileUpdate(Map<String, String> bodyJson) {
//        try {
//            String email = bodyJson.get("email");
//            String name = bodyJson.get("name");
//            String target = bodyJson.get("target");
//            String preferableActivity = bodyJson.get("preferableActivity");
//
//            // Call the service method
//            String updateMessage = profileUpdateService.updateUserProfile(email, name, target, preferableActivity);
//
//            // Return success response
//            return createSuccessResponse(200, "{\"message\":\"" + updateMessage + "\"}");
//        } catch (Exception e) {
//            // Return error response
//            return createErrorResponse(500, "Error updating profile: " + e.getMessage());
//        }
//    }
//
//
//
//    private APIGatewayV2HTTPResponse createErrorResponse(int statusCode, String message) {
//        return APIGatewayV2HTTPResponse.builder()
//                .withStatusCode(statusCode)
//                .withBody("{\"error\":\"" + message + "\"}")
//                .build();
//    }
//
//    private APIGatewayV2HTTPResponse createSuccessResponse(int statusCode, String message) {
//        return APIGatewayV2HTTPResponse.builder()
//                .withStatusCode(statusCode)
//                .withBody(message)
//                .build();
//    }
//
//    public record APIRequest(String method, String path, String authorization_header, Map<String, String> body_json) {}
//}
//
//
//


package com.gymapplication.handler;

import com.gymapplication.di.DaggerApiComponent;
import com.gymapplication.service.ProfileUpdateService;
import com.gymapplication.service.SignupService;
import com.gymapplication.service.SigninService;
import com.gymapplication.service.CognitoService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import javax.inject.Inject;
import java.util.Map;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        runtime = DeploymentRuntime.JAVA17,
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
        @EnvironmentVariable(key = "region", value = "${region}"),
        @EnvironmentVariable(key = "tables_table", value = "${tables_table}"),
        @EnvironmentVariable(key = "booking_userpool", value = "${booking_userpool}")
})
public class ApiHandler implements RequestHandler<ApiHandler.APIRequest, APIGatewayV2HTTPResponse> {

    @Inject
    SignupService signupService;

    @Inject
    SigninService signinService;

    @Inject
    CognitoService cognitoService;

    @Inject
    ProfileUpdateService profileUpdateService;

    public ApiHandler() {
        DaggerApiComponent.create().inject(this);
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIRequest requestEvent, Context context) {
        System.out.println("API request received: " + requestEvent);

        if (requestEvent.body_json() == null || requestEvent.body_json().isEmpty()) {
            return createErrorResponse(400, "Invalid request body");
        }

        String userPoolName = System.getenv("booking_userpool");
        String userPoolId = cognitoService.getUserPoolId(userPoolName);
        if (userPoolId == null || userPoolId.isEmpty()) {
            return createErrorResponse(500, "Unable to retrieve user pool ID");
        }

        switch (requestEvent.path()) {
            case "/signup":
                return handleSignup(requestEvent.body_json(), userPoolId);
            case "/signin":
                return handleSignin(requestEvent.body_json(), userPoolId);
            case "/updateProfile":
                return handleProfileUpdate(requestEvent.body_json());
            default:
                return createErrorResponse(404, "Endpoint not found");
        }
    }

    private APIGatewayV2HTTPResponse handleSignup(Map<String, String> bodyJson, String userPoolId) {
        try {
            String error = signupService.signUpUser(bodyJson, userPoolId);
            if (error != null) {
                return createErrorResponse(400, error);
            }
            return createSuccessResponse(201, "{\"message\":\"User created successfully\"}");
        } catch (Exception e) {
            return createErrorResponse(500, "Error processing signup: " + e.getMessage());
        }
    }

    private APIGatewayV2HTTPResponse handleSignin(Map<String, String> bodyJson, String userPoolId) {
        try {
            String clientId = cognitoService.createAppClient(userPoolId);
            if (clientId == null) {
                return createErrorResponse(500, "Unable to create app client");
            }
            String idToken = signinService.signInUser(bodyJson, userPoolId, clientId);
            if (idToken == null) {
                return createErrorResponse(401, "Incorrect username or password.");
            }
            return createSuccessResponse(200, "{\"id_token\":\"" + idToken + "\"}");
        } catch (Exception e) {
            return createErrorResponse(500, "Error processing signin: " + e.getMessage());
        }
    }

    private APIGatewayV2HTTPResponse handleProfileUpdate(Map<String, String> bodyJson) {
        try {
            String email = bodyJson.get("email");
            String name = bodyJson.get("name");
            String target = bodyJson.get("target");
            String preferableActivity = bodyJson.get("preferableActivity");

            // Call the service method
            String updateMessage = profileUpdateService.updateUserProfile(email, name, target, preferableActivity);

            // Return success response
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withBody("{\"message\":\"" + updateMessage + "\"}")
                    .build();
        } catch (Exception e) {
            // Return error response
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(500)
                    .withBody("{\"message\":\"Error updating profile: " + e.getMessage() + "\"}")
                    .build();
        }
    }



    private APIGatewayV2HTTPResponse createErrorResponse(int statusCode, String message) {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withBody("{\"error\":\"" + message + "\"}")
                .build();
    }

    private APIGatewayV2HTTPResponse createSuccessResponse(int statusCode, String message) {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withBody(message)
                .build();
    }

    public record APIRequest(String method, String path, String authorization_header, Map<String, String> body_json) {}
}


