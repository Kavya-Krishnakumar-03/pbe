package com.gymapplication.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.gymapplication.service.ProfileUpdateService;
import com.gymapplication.service.SignupService;
import com.gymapplication.service.SigninService;
import com.gymapplication.service.CognitoService;
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
import java.util.Map;

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
public class ApiHandler implements RequestHandler<ApiHandler.APIRequest, APIGatewayProxyResponseEvent> {

    @Inject
    SignupService signupService;

    @Inject
    SigninService signinService;

    @Inject
    ProfileUpdateService profileUpdateService;

    @Inject
    CognitoService cognitoService;

    public ApiHandler() {
        DaggerApiComponent.create().inject(this);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIRequest requestEvent, Context context) {
        System.out.println("API request received: " + requestEvent);

        if (requestEvent.body_json() == null || requestEvent.body_json().isEmpty()) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_BAD_REQUEST + ",\"error\":\"Invalid request body\"}")
                    .withHeaders(getCorsHeaders());
        }

        String userPoolName = System.getenv("booking_userpool");
        String userPoolId = cognitoService.getUserPoolId(userPoolName);
        if (userPoolId == null || userPoolId.isEmpty()) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ",\"error\":\"Unable to retrieve user pool ID\"}")
                    .withHeaders(getCorsHeaders());
        }

        switch (requestEvent.path()) {
            case "/signup":
                return handleSignup(requestEvent.body_json(), userPoolId);
            case "/signin":
                return handleSignin(requestEvent.body_json(), userPoolId);
            case "/updateProfile":
                return handleUpdateProfile(requestEvent.body_json());
            default:
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(HttpStatus.SC_NOT_FOUND)
                        .withBody("{\"statusCode\":" + HttpStatus.SC_NOT_FOUND + ",\"error\":\"Endpoint not found\"}")
                        .withHeaders(getCorsHeaders());
        }
    }

    private APIGatewayProxyResponseEvent handleSignup(Map<String, String> bodyJson, String userPoolId) {
        System.out.println("Handling signup request and bodyJson is: " + bodyJson);

        String error = signupService.signUpUser(bodyJson, userPoolId);
        if (error != null) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_BAD_REQUEST + ",\"error\":\"" + error + "\"}")
                    .withHeaders(getCorsHeaders());
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatus.SC_OK)
                .withBody("{\"message\":\"User created successfully\"}")
                .withHeaders(getCorsHeaders());
    }

    private APIGatewayProxyResponseEvent handleSignin(Map<String, String> bodyJson, String userPoolId) {
        String clientId = cognitoService.createAppClient(userPoolId);
        if (clientId == null) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ",\"error\":\"Unable to create app client\"}")
                    .withHeaders(getCorsHeaders());
        }
        return signinService.signInUser(bodyJson, userPoolId, clientId)
                .withHeaders(getCorsHeaders());
    }

    private APIGatewayProxyResponseEvent handleUpdateProfile(Map<String, String> bodyJson) {
        String email = bodyJson.get("email");
        String name = bodyJson.get("name");
        String target = bodyJson.get("target");
        String preferableActivity = bodyJson.get("preferableActivity");

        if (email == null || email.isEmpty()) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_BAD_REQUEST)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_BAD_REQUEST + ",\"error\":\"Email is required\"}")
                    .withHeaders(getCorsHeaders());
        }

        String result = profileUpdateService.updateUserProfile(email, name, target, preferableActivity);
        if (result.startsWith("Error")) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .withBody("{\"statusCode\":" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ",\"error\":\"" + result + "\"}")
                    .withHeaders(getCorsHeaders());
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatus.SC_OK)
                .withBody("{\"message\":\"" + result + "\"}")
                .withHeaders(getCorsHeaders());
    }

    private Map<String, String> getCorsHeaders() {
        return Map.of(
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "POST, GET, OPTIONS",
                "Access-Control-Allow-Headers", "Content-Type, Authorization"
        );
    }

    public record APIRequest(String method, String path, String authorization_header, Map<String, String> body_json) {}
}
