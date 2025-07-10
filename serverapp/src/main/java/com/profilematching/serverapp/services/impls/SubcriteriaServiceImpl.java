package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.SubcriteriaResponse;
import com.profilematching.serverapp.models.entities.Criteria;
import com.profilematching.serverapp.models.entities.Subcriteria;
import com.profilematching.serverapp.repositories.CriteriaRepository;
import com.profilematching.serverapp.repositories.SubcriteriaRepository;
import com.profilematching.serverapp.services.SubcriteriaService;
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
public class SubcriteriaServiceImpl implements SubcriteriaService {

    @Autowired
    private SubcriteriaRepository subcriteriaRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SubcriteriaResponse> getAllSubcriteria() {
        log.info("Retrieving all subcriteria");

        return subcriteriaRepository.findAll().stream()
                .map(this::mapToSubcriteriaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubcriteriaResponse getSubcriteriaById(Integer Id) {
        log.info("Retrieving subcriteria data for ID: {}", Id);

        return subcriteriaRepository.findById(Id)
                .map(this::mapToSubcriteriaResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcriteria not found!"));
    }

    @Override
    public SubcriteriaResponse addSubcriteria(AddSubcriteriaRequest addSubcriteriaRequest) {
        log.info("Adding new subcriteria: {}", addSubcriteriaRequest.getCode());

        if (subcriteriaRepository.existsByCode(addSubcriteriaRequest.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subcriteria code already exists!");
        }

        Criteria criteria = criteriaRepository.findById(addSubcriteriaRequest.getCriteriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        Subcriteria subcriteria = new Subcriteria();
        subcriteria.setCode(addSubcriteriaRequest.getCode());
        subcriteria.setDescription(addSubcriteriaRequest.getDescription());
        subcriteria.setTarget(addSubcriteriaRequest.getTarget());
        subcriteria.setType(addSubcriteriaRequest.getType());
        subcriteria.setCriteria(criteria);
        subcriteriaRepository.save(subcriteria);

        log.info("Completed adding new subcriteria: {}", subcriteria.getCode());
        return mapToSubcriteriaResponse(subcriteria);
    }

    @Override
    public SubcriteriaResponse updateSubcriteria(Integer Id, UpdateSubcriteriaRequest updateSubcriteriaRequest) {
        log.info("Updating subcriteria with ID: {}", Id);

        Subcriteria subcriteria = subcriteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcriteria not found!"));

        if (subcriteriaRepository.existsByCodeAndNotId(updateSubcriteriaRequest.getCode(), Id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subcriteria code already exists!");
        }

        Criteria criteria = criteriaRepository.findById(updateSubcriteriaRequest.getCriteriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criteria not found!"));

        subcriteria.setCode(updateSubcriteriaRequest.getCode());
        subcriteria.setDescription(updateSubcriteriaRequest.getDescription());
        subcriteria.setTarget(updateSubcriteriaRequest.getTarget());
        subcriteria.setType(updateSubcriteriaRequest.getType());
        subcriteria.setCriteria(criteria);
        subcriteriaRepository.save(subcriteria);

        log.info("Update successful for subcriteria ID: {}", subcriteria.getId());
        return mapToSubcriteriaResponse(subcriteria);
    }

    @Override
    public SubcriteriaResponse deleteSubcriteria(Integer Id) {
        log.info("Deleting subcriteria with ID: {}", Id);

        Subcriteria subcriteria = subcriteriaRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subcriteria not found!"));

        subcriteriaRepository.delete(subcriteria);

        log.info("Delete successful for subcriteria ID: {}", subcriteria.getId());
        return mapToSubcriteriaResponse(subcriteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSubcriteria() {
        log.info("Counting total number of subcriteria");

        return subcriteriaRepository.countSubcriteria();
    }

    private SubcriteriaResponse mapToSubcriteriaResponse(Subcriteria subcriteria) {
        return SubcriteriaResponse.builder()
                .id(subcriteria.getId())
                .code(subcriteria.getCode())
                .description(subcriteria.getDescription())
                .target(subcriteria.getTarget())
                .type(subcriteria.getType())
                .criteriaId(subcriteria.getCriteria().getId())
                .criteriaName(subcriteria.getCriteria().getName())
                .build();
    }
}