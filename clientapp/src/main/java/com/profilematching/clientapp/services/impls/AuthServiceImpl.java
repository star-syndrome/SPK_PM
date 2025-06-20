package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.requests.LoginRequest;
import com.profilematching.clientapp.models.dtos.responses.LoginResponse;
import com.profilematching.clientapp.services.AuthService;
import com.profilematching.clientapp.utils.AuthSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Boolean login(LoginRequest loginRequest) {
        try {
            String url = "http://localhost:8080/api/auth";
            ResponseEntity<LoginResponse> response = restTemplate.exchange(
                    url + "/login",
                    HttpMethod.POST,
                    new HttpEntity<>(loginRequest),
                    LoginResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                assert response.getBody() != null;
                setPrinciple(response.getBody(), loginRequest.getPassword());

                Authentication authentication = AuthSessionUtil.getAuthentication();

                log.info("Username: {}", authentication.getName());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void setPrinciple(LoginResponse loginResponse, String password) {
        List<SimpleGrantedAuthority> authorities = loginResponse.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginResponse.getUsername(), password, authorities
        );

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}