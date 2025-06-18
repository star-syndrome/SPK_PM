package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.responses.RankingResponse;
import com.profilematching.serverapp.models.entities.Candidate;

import java.util.List;
import java.util.Map;

public interface RankingService {

     List<RankingResponse> getAllRankings();

     void calculateAllRankings();

     Double calculateTotalScore(Candidate candidate);

     Double convertGapToValue(Double gap);

     Double calculateAverage(List<Double> values);

     Double calculateFinalScore(Double cf, Double sf);

     Map<String, Double> getBobotKriteriaMap();

     void processCandidateScores(Candidate candidate, Map<String, List<Double>> cfMap, Map<String, List<Double>> sfMap);
}