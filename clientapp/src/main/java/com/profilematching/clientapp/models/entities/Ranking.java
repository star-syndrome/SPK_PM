package com.profilematching.clientapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {

    private Integer id;
    private Double totalScore;
    private Integer rankingOrder;
    private Candidate candidate;
}