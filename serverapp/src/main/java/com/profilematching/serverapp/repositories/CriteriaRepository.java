package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {

    Boolean existsByCode(String code);

    @Query("SELECT COUNT(c) > 0 FROM Criteria c WHERE c.code = :code AND c.id != :id")
    Boolean existsByCodeAndNotId(String code, Integer id);

    @Query("SELECT COUNT(c) FROM Criteria c")
    Long countTotalCriteria();
}