package com.profilematching.clientapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCandidateRequest {

    private String name;
    private String dateOfBirth;
    private String gender;
    private String phone;
    private String address;
}