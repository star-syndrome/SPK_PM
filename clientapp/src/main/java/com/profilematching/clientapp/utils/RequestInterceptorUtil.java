package com.profilematching.clientapp.utils;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public class RequestInterceptorUtil implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication authentication = AuthSessionUtil.getAuthentication();

        if (!request.getURI().getPath().equals("/api/auth/login")) {
            request.getHeaders().add(
                    "Authorization", "Basic " + BasicHeaderUtil.createBasicToken(
                            authentication.getName(), authentication.getCredentials().toString()));
        }

        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }
}