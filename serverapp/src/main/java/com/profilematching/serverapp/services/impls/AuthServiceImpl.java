package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.ForgotPasswordRequest;
import com.profilematching.serverapp.models.dtos.requests.LoginRequest;
import com.profilematching.serverapp.models.dtos.requests.RegistrationRequest;
import com.profilematching.serverapp.models.dtos.responses.LoginResponse;
import com.profilematching.serverapp.models.dtos.responses.RegistrationResponse;
import com.profilematching.serverapp.models.dtos.responses.UserResponse;
import com.profilematching.serverapp.models.entities.Role;
import com.profilematching.serverapp.models.entities.User;
import com.profilematching.serverapp.repositories.RoleRepository;
import com.profilematching.serverapp.repositories.UserRepository;
import com.profilematching.serverapp.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Trying to log in user: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        User users = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("Successfully signed in, user: {}", userDetails.getUsername());
        return LoginResponse.builder()
                .id(users.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {
        log.info("Registering new user: {}", registrationRequest.getUsername());

        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        User users = new User();
        users.setUsername(registrationRequest.getUsername());

        List<Role> roles = Collections.singletonList(roleRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")));

        users.setRoles(roles);
        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userRepository.save(users);

        log.info("Successfully registered user: {}", registrationRequest.getUsername());
        return RegistrationResponse.builder()
                .id(users.getId())
                .username(users.getUsername())
                .build();
    }

    @Override
    public UserResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Attempting to reset password for user: {}", forgotPasswordRequest.getUsername());

        User user = userRepository.findByUsername(forgotPasswordRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (!forgotPasswordRequest.getNewPassword().equals(forgotPasswordRequest.getRepeatNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match!");
        }

        String encode = passwordEncoder.encode(forgotPasswordRequest.getNewPassword());
        user.setPassword(encode);
        userRepository.save(user);

        log.info("Password reset successful for user: {}", user.getUsername());
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}