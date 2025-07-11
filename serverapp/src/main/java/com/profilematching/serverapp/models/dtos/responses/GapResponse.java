package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GapResponse {

    private String candidateName;
    private String criteriaName;
    private String subcriteriaCode;
    private Double score;
    private Double target;
    private Double gap;
    private Double convertedValue;
}