package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Ranking r WHERE r.candidate.id = :candidateId")
    void deleteByCandidateId(@Param("candidateId") Integer candidateId);
}