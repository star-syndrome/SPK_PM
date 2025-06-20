package com.profilematching.clientapp.controllers;

import com.profilematching.clientapp.models.dtos.requests.LoginRequest;
import com.profilematching.clientapp.utils.AuthSessionUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String loginView(LoginRequest loginRequest) {
        Authentication authentication = AuthSessionUtil.getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return "pages/auth/login";
        }
        return "redirect:/home";
    }

    @GetMapping(
            path = "/registration",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String registrationView() {
        return "pages/auth/registration";
    }

    @GetMapping(
            path = "/forgot-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String forgotPasswordView() {
        return "pages/auth/forgot-password";
    }
}