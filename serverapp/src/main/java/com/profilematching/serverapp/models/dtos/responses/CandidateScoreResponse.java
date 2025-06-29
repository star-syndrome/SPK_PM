package com.profilematching.serverapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateScoreResponse {

    private Integer id;
    private Double score;
    private Integer candidateId;
    private String candidateName;
    private String subcriteriaCode;
    private String subcriteriaDescription;
}