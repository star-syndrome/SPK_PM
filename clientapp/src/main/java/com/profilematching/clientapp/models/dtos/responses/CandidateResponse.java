package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {

    private Integer id;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String phone;
    private String address;
}