package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CFandSFResponse {

    private String candidateName;
    private String criteriaCode;
    private Double cf;
    private Double sf;
    private Double finalScore;
}