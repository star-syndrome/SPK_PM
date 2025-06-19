package com.profilematching.clientapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
    private User user;
}