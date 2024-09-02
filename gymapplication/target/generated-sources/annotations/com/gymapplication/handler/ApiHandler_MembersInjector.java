package com.gymapplication.handler;

import com.gymapplication.service.CoachService;
import com.gymapplication.service.CognitoService;
import com.gymapplication.service.SigninService;
import com.gymapplication.service.SignupService;
import com.gymapplication.service.UpdateService;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
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
public final class ApiHandler_MembersInjector implements MembersInjector<ApiHandler> {
  private final Provider<SignupService> signupServiceProvider;

  private final Provider<SigninService> signinServiceProvider;

  private final Provider<CognitoService> cognitoServiceProvider;

  private final Provider<UpdateService> updateServiceProvider;

  private final Provider<CoachService> coachServiceProvider;

  public ApiHandler_MembersInjector(Provider<SignupService> signupServiceProvider,
      Provider<SigninService> signinServiceProvider,
      Provider<CognitoService> cognitoServiceProvider,
      Provider<UpdateService> updateServiceProvider, Provider<CoachService> coachServiceProvider) {
    this.signupServiceProvider = signupServiceProvider;
    this.signinServiceProvider = signinServiceProvider;
    this.cognitoServiceProvider = cognitoServiceProvider;
    this.updateServiceProvider = updateServiceProvider;
    this.coachServiceProvider = coachServiceProvider;
  }

  public static MembersInjector<ApiHandler> create(Provider<SignupService> signupServiceProvider,
      Provider<SigninService> signinServiceProvider,
      Provider<CognitoService> cognitoServiceProvider,
      Provider<UpdateService> updateServiceProvider, Provider<CoachService> coachServiceProvider) {
    return new ApiHandler_MembersInjector(signupServiceProvider, signinServiceProvider, cognitoServiceProvider, updateServiceProvider, coachServiceProvider);
  }

  @Override
  public void injectMembers(ApiHandler instance) {
    injectSignupService(instance, signupServiceProvider.get());
    injectSigninService(instance, signinServiceProvider.get());
    injectCognitoService(instance, cognitoServiceProvider.get());
    injectUpdateService(instance, updateServiceProvider.get());
    injectCoachService(instance, coachServiceProvider.get());
  }

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.signupService")
  public static void injectSignupService(ApiHandler instance, SignupService signupService) {
    instance.signupService = signupService;
  }

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.signinService")
  public static void injectSigninService(ApiHandler instance, SigninService signinService) {
    instance.signinService = signinService;
  }

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.cognitoService")
  public static void injectCognitoService(ApiHandler instance, CognitoService cognitoService) {
    instance.cognitoService = cognitoService;
  }

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.updateService")
  public static void injectUpdateService(ApiHandler instance, UpdateService updateService) {
    instance.updateService = updateService;
  }

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.coachService")
  public static void injectCoachService(ApiHandler instance, CoachService coachService) {
    instance.coachService = coachService;
  }
}
