package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {

    private String candidateName;
    private Double totalScore;
    private Integer rankingOrder;
}