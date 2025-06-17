package com.profilematching.serverapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCandidateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String dateOfBirth;

    @NotBlank
    @Size(max = 1)
    private String gender;

    @NotBlank
    @Size(max = 13)
    private String phone;

    @NotBlank
    private String address;
}