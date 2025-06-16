package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.serverapp.models.entities.Criteria;
import com.profilematching.serverapp.repositories.CriteriaRepository;
import com.profilematching.serverapp.services.CriteriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CriteriaResponse> getAllCriteria() {
        log.info("Retrieving all criteria");

        return criteriaRepository.findAll().stream()
                .map(this::mapToCriteriaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CriteriaResponse getCriteriaById(Integer Id) {
        log.info("Retrieving criteria data for ID: {}", Id);

        return criteriaRepository.findById(Id)
                .map(this::mapToCriteriaResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));
    }

    @Override
    public CriteriaResponse addCriteria(AddCriteriaRequest addCriteriaRequest) {
        log.info("Adding new criteria: {}", addCriteriaRequest.getName());

        if (criteriaRepository.existsByCode(addCriteriaRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criteria code already exists!");
        }

        Double totalWeight = criteriaRepository.sumAllWeight();
        if (totalWeight + addCriteriaRequest.getWeight() > 1.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total weight cannot exceed 1");
        }

        Criteria criteria = new Criteria();
        criteria.setCode(addCriteriaRequest.getCode());
        criteria.setName(addCriteriaRequest.getName());
        criteria.setWeight(addCriteriaRequest.getWeight());
        criteriaRepository.save(criteria);

        log.info("Completed adding new criteria: {}", criteria.getName());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    public CriteriaResponse updateCriteria(Integer Id, UpdateCriteriaRequest updateCriteriaRequest) {
        log.info("Updating criteria with ID: {}", Id);

        Criteria criteria = criteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        if (criteriaRepository.existsByCodeAndNotId(updateCriteriaRequest.getCode(), Id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criteria code already exists!");
        }

        Double totalOtherWeights = criteriaRepository.sumAllWeightExcludingId(Id);
        Double updatedWeight = updateCriteriaRequest.getWeight();

        if (totalOtherWeights + updatedWeight > 1.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total weight cannot exceed 1");
        }

        criteria.setCode(updateCriteriaRequest.getCode());
        criteria.setName(updateCriteriaRequest.getName());
        criteria.setWeight(updatedWeight);
        criteriaRepository.save(criteria);

        log.info("Update successful for criteria ID: {}", criteria.getId());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    public CriteriaResponse deleteCriteria(Integer Id) {
        log.info("Deleting criteria with ID: {}", Id);

        Criteria criteria = criteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        criteriaRepository.delete(criteria);

        log.info("Delete successful for criteria ID: {}", criteria.getId());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCriteria() {
        log.info("Counting total number of criteria");

        return criteriaRepository.countCriteria();
    }

    private CriteriaResponse mapToCriteriaResponse(Criteria criteria) {
        return CriteriaResponse.builder()
                .id(criteria.getId())
                .name(criteria.getName())
                .code(criteria.getCode())
                .weight(criteria.getWeight())
                .build();
    }
}