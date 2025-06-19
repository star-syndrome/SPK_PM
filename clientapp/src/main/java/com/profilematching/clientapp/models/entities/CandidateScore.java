package com.profilematching.clientapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateScore {

    private Integer id;
    private Double score;
    private Candidate candidate;
    private Subcriteria subcriteria;
}