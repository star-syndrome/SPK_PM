package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateResponse;
import com.profilematching.serverapp.models.entities.Candidate;
import com.profilematching.serverapp.repositories.CandidateRepository;
import com.profilematching.serverapp.services.CandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateResponse> getAllCandidate() {
        log.info("Retrieving all candidate");

        return candidateRepository.findAll().stream()
                .map(this::mapToCandidateResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateResponse getCandidateById(Integer Id) {
        log.info("Retrieving candidate data for ID: {}", Id);

        return candidateRepository.findById(Id)
                .map(this::mapToCandidateResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found!"));
    }

    @Override
    public CandidateResponse addCandidate(AddCandidateRequest addCandidateRequest) {
        log.info("Adding new candidate: {}", addCandidateRequest.getName());

        Candidate candidate = new Candidate();
        candidate.setName(addCandidateRequest.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate dob = LocalDate.parse(addCandidateRequest.getDateOfBirth(), formatter);
            candidate.setDateOfBirth(dob);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format, use dd-MM-yyyy");
        }

        candidate.setGender(addCandidateRequest.getGender());
        candidate.setPhone(addCandidateRequest.getPhone());
        candidate.setAddress(addCandidateRequest.getAddress());
        candidateRepository.save(candidate);

        log.info("Completed adding new candidate: {}", candidate.getName());
        return mapToCandidateResponse(candidate);
    }

    @Override
    public CandidateResponse updateCandidate(Integer Id, UpdateCandidateRequest updateCandidateRequest) {
        log.info("Updating candidate with ID: {}", Id);

        Candidate candidate = candidateRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found!"));

        candidate.setName(updateCandidateRequest.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate dob = LocalDate.parse(updateCandidateRequest.getDateOfBirth(), formatter);
            candidate.setDateOfBirth(dob);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format, use dd-MM-yyyy");
        }

        candidate.setGender(updateCandidateRequest.getGender());
        candidate.setPhone(updateCandidateRequest.getPhone());
        candidate.setAddress(updateCandidateRequest.getAddress());
        candidateRepository.save(candidate);

        log.info("Update successful for candidate ID: {}", candidate.getId());
        return mapToCandidateResponse(candidate);
    }

    @Override
    public CandidateResponse deleteCandidate(Integer Id) {
        log.info("Deleting candidate with ID: {}", Id);

        Candidate candidate = candidateRepository.findById(Id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found!"));

        candidateRepository.delete(candidate);

        log.info("Delete successful for candidate ID: {}", candidate.getId());
        return mapToCandidateResponse(candidate);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCandidate() {
        log.info("Counting total number of candidate");

        return candidateRepository.countCandidate();
    }

    private CandidateResponse mapToCandidateResponse(Candidate candidate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return CandidateResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .dateOfBirth(candidate.getDateOfBirth().format(formatter))
                .gender(candidate.getGender())
                .phone(candidate.getPhone())
                .address(candidate.getAddress())
                .build();
    }
}