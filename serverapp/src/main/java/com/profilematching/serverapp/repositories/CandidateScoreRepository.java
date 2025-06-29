package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.CandidateScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateScoreRepository extends JpaRepository<CandidateScore, Integer> {

    List<CandidateScore> findByCandidateId(Integer candidateId);

    Optional<CandidateScore> findByCandidateIdAndSubcriteriaId(Integer candidateId, Integer subcriteriaId);
}