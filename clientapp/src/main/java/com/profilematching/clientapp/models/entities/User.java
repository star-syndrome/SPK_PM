package com.profilematching.clientapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    private String username;
    private String password;
    private List<Role> roles;
}