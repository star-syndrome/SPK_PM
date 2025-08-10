package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.BulkCandidateScoreRequest;
import com.profilematching.clientapp.models.dtos.responses.*;

import java.util.List;

public interface CandidateScoreService {

    List<CandidateScoreResponse> getAllCandidateScore();

    List<GapConversionResponse> getGapConversions();

    List<GapResponse> getAllGapDetails();

    List<CFandSFResponse> getCFandSFDetails();

    List<FinalScoreDetailResponse> getFinalScoreDetails();

    List<TotalFinalScoreResponse> getTotalFinalScores();

    List<CandidateScoreResponse> getScoresByCandidateId(Integer Id);

    CandidateScoreResponse getCandidateScoreById(Integer Id);

    String saveOrUpdateBulkScores(BulkCandidateScoreRequest request);

    CandidateScoreResponse deleteCandidateScore(Integer Id);

    byte[] generateCandidateScorePdf() throws Exception;
}