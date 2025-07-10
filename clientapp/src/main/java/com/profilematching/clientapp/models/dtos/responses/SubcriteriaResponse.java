package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcriteriaResponse {

    private Integer id;
    private String code;
    private String description;
    private String type;
    private Double target;
    private Integer criteriaId;
    private String criteriaName;
}