package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateResponse;
import com.profilematching.serverapp.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping(
            path = "/candidate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateResponse>> getAllCandidate() {
        return ResponseEntity.ok()
                .body(candidateService.getAllCandidate());
    }

    @GetMapping(
            path = "/candidate/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateService.getCandidateById(Id));
    }

    @GetMapping(
            path = "/candidate/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> countCandidate() {
        return ResponseEntity.ok()
                .body(candidateService.countCandidate());
    }

    @PostMapping(
            path = "/candidate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> addCandidate(@Validated @RequestBody AddCandidateRequest addCandidateRequest){
        return ResponseEntity.ok()
                .body(candidateService.addCandidate(addCandidateRequest));
    }

    @PutMapping(
            path = "/candidate/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> updateCandidate(
            @PathVariable Integer Id, @Validated @RequestBody UpdateCandidateRequest updateCandidateRequest
    ){
        return ResponseEntity.ok()
                .body(candidateService.updateCandidate(Id, updateCandidateRequest));
    }

    @DeleteMapping(
            path = "/candidate/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> deleteCandidate(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateService.deleteCandidate(Id));
    }
}