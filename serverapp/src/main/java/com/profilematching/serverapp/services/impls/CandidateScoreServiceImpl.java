package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.BulkCandidateScoreRequest;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
    public List<CandidateScoreResponse> getScoresByCandidateId(Integer Id) {
        log.info("Retrieving candidate score data for candidate ID: {}", Id);

        List<CandidateScore> scores = candidateScoreRepository.findByCandidateId(Id);

        if (scores.isEmpty()) {
            throw new EntityNotFoundException("No scores found for candidate ID: " + Id);
        }

        return scores.stream()
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
    public String saveOrUpdateBulkScores(BulkCandidateScoreRequest request) {
        log.info("Adding or updating candidate score for candidate ID: {}", request.getCandidateId());

        Integer candidateId = request.getCandidateId();
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with ID: " + candidateId));

        for (BulkCandidateScoreRequest.ScoreEntry entry : request.getScores()) {
            Subcriteria subcriteria = subcriteriaRepository.findById(entry.getSubcriteriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Subcriteria not found with ID: " + entry.getSubcriteriaId()));

            Optional<CandidateScore> existing = candidateScoreRepository
                    .findByCandidateIdAndSubcriteriaId(candidateId, entry.getSubcriteriaId());

            CandidateScore score = existing.orElseGet(CandidateScore::new);
            score.setCandidate(candidate);
            score.setSubcriteria(subcriteria);
            score.setScore(entry.getScore());

            candidateScoreRepository.save(score);
        }

        log.info("Completed adding or updating candidate score for candidate: {}", candidate.getName());
        return "Completed adding or updating candidate score";
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
                .candidateId(candidateScore.getCandidate().getId())
                .candidateName(candidateScore.getCandidate().getName())
                .subcriteriaCode(candidateScore.getSubcriteria().getCode())
                .subcriteriaDescription(candidateScore.getSubcriteria().getDescription())
                .build();
    }
}