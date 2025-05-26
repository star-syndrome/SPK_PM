package com.profilematching.serverapp.repositories;

import com.profilematching.serverapp.models.entities.Subcriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcriteriaRepository extends JpaRepository<Subcriteria, Integer> {

}