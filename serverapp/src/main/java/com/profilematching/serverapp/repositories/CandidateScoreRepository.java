package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.CandidateScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateScoreRepository extends JpaRepository<CandidateScore, Integer> {

    List<CandidateScore> findByCandidateId(Integer candidateId);

    Optional<CandidateScore> findByCandidateIdAndSubcriteriaId(Integer candidateId, Integer subcriteriaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CandidateScore cs WHERE cs.candidate.id = :candidateId")
    void deleteByCandidateId(@Param("candidateId") Integer candidateId);
}