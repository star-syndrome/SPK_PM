package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalScoreDetailResponse {

    private String candidateName;
    private String criteriaCode;
    private Double cf;
    private Double sf;
    private Double finalScore;
    private Double weight;
    private Double finalScoreXWeight;
}