package com.profilematching.serverapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubcriteriaRequest {

    @NotBlank
    @Size(max = 3)
    private String code;

    @NotBlank
    private String description;

    @NotBlank
    private String type;

    @NotNull
    private Double target;

    @NotNull
    private Integer criteriaId;
}