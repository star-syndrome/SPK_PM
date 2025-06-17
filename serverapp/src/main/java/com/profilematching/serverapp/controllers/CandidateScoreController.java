package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.serverapp.services.CandidateScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/candidate")
public class CandidateScoreController {

    @Autowired
    private CandidateScoreService candidateScoreService;

    @GetMapping(
            path = "/score",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateScoreResponse>> getAllCandidateScore() {
        return ResponseEntity.ok()
                .body(candidateScoreService.getAllCandidateScore());
    }

    @GetMapping(
            path = "/score/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> getCandidateScoreById(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateScoreService.getCandidateScoreById(Id));
    }

    @PostMapping(
            path = "/score",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> addCandidateScore(@Validated @RequestBody AddCandidateScoreRequest addCandidateScoreRequest){
        return ResponseEntity.ok()
                .body(candidateScoreService.addCandidateScore(addCandidateScoreRequest));
    }

    @PutMapping(
            path = "/score/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> updateCandidateScore(
            @PathVariable Integer Id, @Validated @RequestBody UpdateCandidateScoreRequest updateCandidateScoreRequest
    ){
        return ResponseEntity.ok()
                .body(candidateScoreService.updateCandidateScore(Id, updateCandidateScoreRequest));
    }

    @DeleteMapping(
            path = "/score/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> deleteCandidateScore(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateScoreService.deleteCandidateScore(Id));
    }
}