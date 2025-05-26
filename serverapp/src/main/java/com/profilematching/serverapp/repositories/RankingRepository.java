package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Integer> {

}