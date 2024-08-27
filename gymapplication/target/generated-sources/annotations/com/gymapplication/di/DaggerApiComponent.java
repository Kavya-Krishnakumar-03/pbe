package com.gymapplication.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.gymapplication.handler.ApiHandler;
import com.gymapplication.handler.ApiHandler_MembersInjector;
import com.gymapplication.service.CognitoService;
import com.gymapplication.service.ProfileUpdateService;
import com.gymapplication.service.SigninService;
import com.gymapplication.service.SignupService;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DaggerApiComponent {
  private DaggerApiComponent() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static ApiComponent create() {
    return new Builder().build();
  }

  public static final class Builder {
    private ApiModule apiModule;

    private ServiceModule serviceModule;

    private Builder() {
    }

    public Builder apiModule(ApiModule apiModule) {
      this.apiModule = Preconditions.checkNotNull(apiModule);
      return this;
    }

    public Builder serviceModule(ServiceModule serviceModule) {
      this.serviceModule = Preconditions.checkNotNull(serviceModule);
      return this;
    }

    public ApiComponent build() {
      if (apiModule == null) {
        this.apiModule = new ApiModule();
      }
      if (serviceModule == null) {
        this.serviceModule = new ServiceModule();
      }
      return new ApiComponentImpl(apiModule, serviceModule);
    }
  }

  private static final class ApiComponentImpl implements ApiComponent {
    private final ApiComponentImpl apiComponentImpl = this;

    private Provider<CognitoIdentityProviderClient> provideCognitoClientProvider;

    private Provider<AmazonDynamoDB> provideDynamoDBProvider;

    private Provider<SignupService> provideSignupServiceProvider;

    private Provider<SigninService> provideSigninServiceProvider;

    private ApiComponentImpl(ApiModule apiModuleParam, ServiceModule serviceModuleParam) {

      initialize(apiModuleParam, serviceModuleParam);

    }

    private ProfileUpdateService profileUpdateService() {
      return new ProfileUpdateService(provideDynamoDBProvider.get());
    }

    private CognitoService cognitoService() {
      return new CognitoService(provideCognitoClientProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApiModule apiModuleParam,
        final ServiceModule serviceModuleParam) {
      this.provideCognitoClientProvider = DoubleCheck.provider(ApiModule_ProvideCognitoClientFactory.create(apiModuleParam));
      this.provideDynamoDBProvider = DoubleCheck.provider(ApiModule_ProvideDynamoDBFactory.create(apiModuleParam));
      this.provideSignupServiceProvider = DoubleCheck.provider(ServiceModule_ProvideSignupServiceFactory.create(serviceModuleParam, provideCognitoClientProvider, provideDynamoDBProvider));
      this.provideSigninServiceProvider = DoubleCheck.provider(ServiceModule_ProvideSigninServiceFactory.create(serviceModuleParam, provideCognitoClientProvider));
    }

    @Override
    public void inject(ApiHandler apiHandler) {
      injectApiHandler(apiHandler);
    }

    @CanIgnoreReturnValue
    private ApiHandler injectApiHandler(ApiHandler instance) {
      ApiHandler_MembersInjector.injectSignupService(instance, provideSignupServiceProvider.get());
      ApiHandler_MembersInjector.injectSigninService(instance, provideSigninServiceProvider.get());
      ApiHandler_MembersInjector.injectProfileUpdateService(instance, profileUpdateService());
      ApiHandler_MembersInjector.injectCognitoService(instance, cognitoService());
      return instance;
    }
  }
}
