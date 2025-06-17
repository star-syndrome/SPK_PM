package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.serverapp.models.entities.CandidateScore;
import com.profilematching.serverapp.repositories.CandidateRepository;
import com.profilematching.serverapp.repositories.CandidateScoreRepository;
import com.profilematching.serverapp.repositories.SubcriteriaRepository;
import com.profilematching.serverapp.services.CandidateScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CandidateScoreServiceImpl implements CandidateScoreService {

    @Autowired
    private CandidateScoreRepository candidateScoreRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SubcriteriaRepository subcriteriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateScoreResponse> getAllCandidateScore() {
        log.info("Retrieving all candidate score");

        return candidateScoreRepository.findAll().stream()
                .map(this::mapToCandidateScoreResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateScoreResponse getCandidateScoreById(Integer Id) {
        log.info("Retrieving candidate score data for ID: {}", Id);

        return candidateScoreRepository.findById(Id)
                .map(this::mapToCandidateScoreResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate score not found!"));
    }

    @Override
    public CandidateScoreResponse addCandidateScore(AddCandidateScoreRequest addCandidateScoreRequest) {
        return null;
    }

    @Override
    public CandidateScoreResponse updateCandidateScore(Integer Id, UpdateCandidateScoreRequest updateCandidateScoreRequest) {
        return null;
    }

    @Override
    public CandidateScoreResponse deleteCandidateScore(Integer Id) {
        return null;
    }

    private CandidateScoreResponse mapToCandidateScoreResponse(CandidateScore candidateScore) {
        return CandidateScoreResponse.builder()
                .id(candidateScore.getId())
                .score(candidateScore.getScore())
                .candidateName(candidateScore.getCandidate().getName())
                .subcriteriaDescription(candidateScore.getSubcriteria().getDescription())
                .build();
    }
}