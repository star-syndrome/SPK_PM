package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.LoginRequest;

public interface AuthService {

    Boolean login(LoginRequest loginRequest);
}