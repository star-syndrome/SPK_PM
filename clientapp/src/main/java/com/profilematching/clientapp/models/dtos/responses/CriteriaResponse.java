package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaResponse {

    private Integer id;
    private String code;
    private String name;
    private Double weight;
}