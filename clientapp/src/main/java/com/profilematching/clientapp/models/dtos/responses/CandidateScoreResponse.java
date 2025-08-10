package com.profilematching.clientapp.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateScoreResponse {

    private Integer id;
    private Double score;
    private Integer candidateId;
    private String candidateName;
    private Integer subcriteriaId;
    private String subcriteriaCode;
    private String subcriteriaDescription;
}