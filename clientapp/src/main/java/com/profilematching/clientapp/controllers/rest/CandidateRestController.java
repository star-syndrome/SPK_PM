package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidate")
public class CandidateRestController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping(
            path = "/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTotalCourses() {
        return ResponseEntity.ok().body(candidateService.countCandidate());
    }
}