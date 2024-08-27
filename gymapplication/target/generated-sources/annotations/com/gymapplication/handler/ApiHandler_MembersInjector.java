package com.gymapplication.handler;

import com.gymapplication.service.CognitoService;
import com.gymapplication.service.ProfileUpdateService;
import com.gymapplication.service.SigninService;
import com.gymapplication.service.SignupService;
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

  private final Provider<ProfileUpdateService> profileUpdateServiceProvider;

  public ApiHandler_MembersInjector(Provider<SignupService> signupServiceProvider,
      Provider<SigninService> signinServiceProvider,
      Provider<CognitoService> cognitoServiceProvider,
      Provider<ProfileUpdateService> profileUpdateServiceProvider) {
    this.signupServiceProvider = signupServiceProvider;
    this.signinServiceProvider = signinServiceProvider;
    this.cognitoServiceProvider = cognitoServiceProvider;
    this.profileUpdateServiceProvider = profileUpdateServiceProvider;
  }

  public static MembersInjector<ApiHandler> create(Provider<SignupService> signupServiceProvider,
      Provider<SigninService> signinServiceProvider,
      Provider<CognitoService> cognitoServiceProvider,
      Provider<ProfileUpdateService> profileUpdateServiceProvider) {
    return new ApiHandler_MembersInjector(signupServiceProvider, signinServiceProvider, cognitoServiceProvider, profileUpdateServiceProvider);
  }

  @Override
  public void injectMembers(ApiHandler instance) {
    injectSignupService(instance, signupServiceProvider.get());
    injectSigninService(instance, signinServiceProvider.get());
    injectCognitoService(instance, cognitoServiceProvider.get());
    injectProfileUpdateService(instance, profileUpdateServiceProvider.get());
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

  @InjectedFieldSignature("com.gymapplication.handler.ApiHandler.profileUpdateService")
  public static void injectProfileUpdateService(ApiHandler instance,
      ProfileUpdateService profileUpdateService) {
    instance.profileUpdateService = profileUpdateService;
  }
}
