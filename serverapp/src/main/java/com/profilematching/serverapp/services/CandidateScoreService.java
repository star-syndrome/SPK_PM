package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;

import java.util.List;

public interface CandidateScoreService {

    List<CandidateScoreResponse> getAllCandidateScore();

    CandidateScoreResponse getCandidateScoreById(Integer Id);

    CandidateScoreResponse addCandidateScore(AddCandidateScoreRequest addCandidateScoreRequest);

    CandidateScoreResponse updateCandidateScore(Integer Id, UpdateCandidateScoreRequest updateCandidateScoreRequest);

    CandidateScoreResponse deleteCandidateScore(Integer Id);
}