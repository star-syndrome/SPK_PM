package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubcriteriaResponse {

    private Integer id;
    private String code;
    private String description;
    private String type;
    private Double target;
    private String criteriaName;
}