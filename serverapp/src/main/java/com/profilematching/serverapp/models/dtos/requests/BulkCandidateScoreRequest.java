package com.profilematching.serverapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkCandidateScoreRequest {

    @NotNull
    private Integer candidateId;

    @NotNull
    private List<ScoreEntry> scores;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreEntry {
        private Integer subcriteriaId;
        private Double score;
    }
}