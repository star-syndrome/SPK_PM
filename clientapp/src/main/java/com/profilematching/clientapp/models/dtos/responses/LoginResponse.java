package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Integer id;
    private String username;
    private List<String> roles;
}