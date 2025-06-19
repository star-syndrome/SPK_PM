package com.profilematching.clientapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCandidateScoreRequest {

    private Double score;
    private Integer candidateId;
    private Integer subcriteriaId;
}