package com.profilematching.clientapp.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkCandidateScoreRequest {

    private Integer candidateId;
    private List<ScoreEntry> scores;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreEntry {
        private Integer subcriteriaId;
        private Double score;
    }
}