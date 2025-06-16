package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Subcriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcriteriaRepository extends JpaRepository<Subcriteria, Integer> {

    Boolean existsByCode(String code);

    @Query("SELECT COUNT(s) > 0 FROM Subcriteria s WHERE s.code = :code AND s.id != :id")
    Boolean existsByCodeAndNotId(String code, Integer id);

    @Query("SELECT COUNT(s) FROM Subcriteria s")
    Long countSubcriteria();
}