package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.requests.BulkCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;

import java.util.List;

public interface CandidateScoreService {

    List<CandidateScoreResponse> getAllCandidateScore();

    List<CandidateScoreResponse> getScoresByCandidateId(Integer Id);

    CandidateScoreResponse getCandidateScoreById(Integer Id);

    String saveOrUpdateBulkScores(BulkCandidateScoreRequest request);

    CandidateScoreResponse deleteCandidateScore(Integer Id);
}