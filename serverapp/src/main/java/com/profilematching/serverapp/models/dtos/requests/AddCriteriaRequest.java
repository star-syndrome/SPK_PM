package com.profilematching.serverapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCriteriaRequest {

    @NotBlank
    @Size(max = 2)
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private Double weight;

}