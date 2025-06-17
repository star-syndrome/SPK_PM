package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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