package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {

    private String candidateName;
    private Double totalScore;
    private Integer rankingOrder;
}