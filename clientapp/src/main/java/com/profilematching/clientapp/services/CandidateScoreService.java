package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.AddCandidateScoreRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCandidateScoreRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateScoreResponse;

import java.util.List;

public interface CandidateScoreService {

    List<CandidateScoreResponse> getAllCandidateScore();

    CandidateScoreResponse getCandidateScoreById(Integer Id);

    CandidateScoreResponse addCandidateScore(AddCandidateScoreRequest addCandidateScoreRequest);

    CandidateScoreResponse updateCandidateScore(Integer Id, UpdateCandidateScoreRequest updateCandidateScoreRequest);

    CandidateScoreResponse deleteCandidateScore(Integer Id);

    byte[] generateCandidateScorePdf() throws Exception;
}