package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.serverapp.models.entities.Candidate;
import com.profilematching.serverapp.models.entities.CandidateScore;
import com.profilematching.serverapp.models.entities.Subcriteria;
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
        log.info("Adding new score for candidate ID: {}", addCandidateScoreRequest.getCandidateId());

        Candidate candidate = candidateRepository.findById(addCandidateScoreRequest.getCandidateId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found!"));

        Subcriteria subcriteria = subcriteriaRepository.findById(addCandidateScoreRequest.getSubcriteriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcriteria not found!"));

        CandidateScore candidateScore = new CandidateScore();
        candidateScore.setScore(addCandidateScoreRequest.getScore());
        candidateScore.setCandidate(candidate);
        candidateScore.setSubcriteria(subcriteria);
        candidateScoreRepository.save(candidateScore);

        log.info("Completed adding new score for candidate name: {}", candidate.getName());
        return mapToCandidateScoreResponse(candidateScore);
    }

    @Override
    public CandidateScoreResponse updateCandidateScore(Integer Id, UpdateCandidateScoreRequest updateCandidateScoreRequest) {
        log.info("Updating score for candidate ID: {}", updateCandidateScoreRequest.getCandidateId());

        CandidateScore candidateScore = candidateScoreRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate score not found!"));

        Candidate candidate = candidateRepository.findById(updateCandidateScoreRequest.getCandidateId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found!"));

        Subcriteria subcriteria = subcriteriaRepository.findById(updateCandidateScoreRequest.getSubcriteriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcriteria not found!"));

        candidateScore.setScore(updateCandidateScoreRequest.getScore());
        candidateScore.setCandidate(candidate);
        candidateScore.setSubcriteria(subcriteria);
        candidateScoreRepository.save(candidateScore);

        log.info("Completed updating new score for candidate name: {}", candidate.getName());
        return mapToCandidateScoreResponse(candidateScore);
    }

    @Override
    public CandidateScoreResponse deleteCandidateScore(Integer Id) {
        log.info("Deleting candidate score with ID: {}", Id);

        CandidateScore candidateScore = candidateScoreRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate score not found!"));

        candidateScoreRepository.delete(candidateScore);

        log.info("Delete successful for candidate score ID: {}", candidateScore.getId());
        return mapToCandidateScoreResponse(candidateScore);
    }

    private CandidateScoreResponse mapToCandidateScoreResponse(CandidateScore candidateScore) {
        return CandidateScoreResponse.builder()
                .id(candidateScore.getId())
                .score(candidateScore.getScore())
                .candidateName(candidateScore.getCandidate().getName())
                .codeSubcriteria(candidateScore.getSubcriteria().getCode())
                .subcriteriaDescription(candidateScore.getSubcriteria().getDescription())
                .build();
    }
}