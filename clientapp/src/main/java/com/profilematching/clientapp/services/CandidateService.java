package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateResponse;

import java.util.List;

public interface CandidateService {

    List<CandidateResponse> getAllCandidates();

    CandidateResponse getCandidateById(Integer Id);

    CandidateResponse addCandidate(AddCandidateRequest addCandidateRequest);

    CandidateResponse updateCandidate(Integer Id, UpdateCandidateRequest updateCandidateRequest);

    CandidateResponse deleteCandidate(Integer Id);

    byte[] generateCandidatePdf() throws Exception;

    Long countCandidate();
}