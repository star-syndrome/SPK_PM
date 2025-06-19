package com.profilematching.clientapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubcriteriaRequest {

    private String code;
    private String description;
    private String type;
    private Double target;
    private Integer criteriaId;
}