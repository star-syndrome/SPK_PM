package com.profilematching.serverapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCandidateScoreRequest {

    @NotNull
    private Double score;

    @NotNull
    private Integer candidateId;

    @NotNull
    private Integer subcriteriaId;
}