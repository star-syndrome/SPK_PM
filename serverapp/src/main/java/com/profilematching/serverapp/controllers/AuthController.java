package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.ForgotPasswordRequest;
import com.profilematching.serverapp.models.dtos.requests.LoginRequest;
import com.profilematching.serverapp.models.dtos.requests.RegistrationRequest;
import com.profilematching.serverapp.models.dtos.responses.LoginResponse;
import com.profilematching.serverapp.models.dtos.responses.RegistrationResponse;
import com.profilematching.serverapp.models.dtos.responses.UserResponse;
import com.profilematching.serverapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8300", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/registration",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RegistrationResponse> registration(@Validated @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok().body(authService.registration(registrationRequest));
    }

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping(
            path = "/forgot-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponse> forgotPassword(@Validated @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.ok().body(authService.forgotPassword(forgotPasswordRequest));
    }
}