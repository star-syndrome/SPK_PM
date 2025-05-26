package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.CandidateScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateScoreRepository extends JpaRepository<CandidateScore, Integer> {

}