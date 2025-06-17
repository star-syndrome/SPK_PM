package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    @Query("SELECT COUNT(c) FROM Candidate c")
    Long countCandidate();
}