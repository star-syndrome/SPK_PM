package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.serverapp.models.entities.Criteria;
import com.profilematching.serverapp.repositories.CriteriaRepository;
import com.profilematching.serverapp.services.CriteriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private CriteriaRepository criteriaRepository;

    private static final Logger log = LoggerFactory.getLogger(CriteriaServiceImpl.class);

    @Override
    public List<CriteriaResponse> getAllCriteria() {
        log.info("Get All Criteria");
        return criteriaRepository.findAll().stream()
                .map(this::mapToCriteriaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CriteriaResponse addCriteria(AddCriteriaRequest addCriteriaRequest) {
        log.info("Process of adding new criteria: {}", addCriteriaRequest.getName());
        if (criteriaRepository.existsByCode(addCriteriaRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criteria code already exists!");
        }

        Criteria criteria = Criteria.builder()
                .code(addCriteriaRequest.getCode())
                .name(addCriteriaRequest.getName())
                .weight(addCriteriaRequest.getWeight())
                .build();
        criteriaRepository.save(criteria);

        log.info("Process of adding a new criteria is completed, new criteria: {}", criteria.getName());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    public CriteriaResponse updateCriteria(Integer Id, UpdateCriteriaRequest updateCriteriaRequest) {
        log.info("Try to update criteria data with id {}", Id);
        Criteria criteria = criteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        if (criteriaRepository.existsByCodeAndNotId(updateCriteriaRequest.getCode(), Id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criteria code already exists!");
        }

        criteria.setCode(updateCriteriaRequest.getCode());
        criteria.setName(updateCriteriaRequest.getName());
        criteria.setWeight(updateCriteriaRequest.getWeight());
        criteriaRepository.save(criteria);

        log.info("Updating the criteria with id {} was successful!", criteria.getId());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    public CriteriaResponse deleteCriteria(Integer Id) {
        log.info("Try to delete criteria data with id {}", Id);
        Criteria criteria = criteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        criteriaRepository.delete(criteria);
        log.info("Deleting the criteria with id {} was successful!", criteria.getId());
        return mapToCriteriaResponse(criteria);
    }

    @Override
    public Long countAllCriteria() {
        log.info("Get total of all criteria!");
        return criteriaRepository.countTotalCriteria();
    }

    private CriteriaResponse mapToCriteriaResponse(Criteria criteria) {
        return CriteriaResponse.builder()
                .name(criteria.getName())
                .code(criteria.getCode())
                .weight(criteria.getWeight())
                .build();
    }
}