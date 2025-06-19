package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.requests.ForgotPasswordRequest;
import com.profilematching.serverapp.models.dtos.requests.LoginRequest;
import com.profilematching.serverapp.models.dtos.requests.RegistrationRequest;
import com.profilematching.serverapp.models.dtos.responses.LoginResponse;
import com.profilematching.serverapp.models.dtos.responses.RegistrationResponse;
import com.profilematching.serverapp.models.dtos.responses.UserResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    UserResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}