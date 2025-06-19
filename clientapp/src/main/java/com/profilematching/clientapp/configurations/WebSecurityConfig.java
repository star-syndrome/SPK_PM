package com.profilematching.clientapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests(auth ->
                        auth
                                .antMatchers("/css/**", "/img/**", "/js/**")
                                .permitAll()
                                .antMatchers("/", "/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(login ->
                        login
                                .loginPage("/auth/login") // Get
                                .loginProcessingUrl("/auth/login") // Post
                                .successForwardUrl("/home")
                                .failureForwardUrl("/auth/login?error=true")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .permitAll()
                )
                .build();
    }
}