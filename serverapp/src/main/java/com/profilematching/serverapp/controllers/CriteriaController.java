package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.serverapp.services.CriteriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CriteriaController {

    @Autowired
    private CriteriaService criteriaService;

    @GetMapping(
            path = "/criteria",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "API to get all criteria")
    public ResponseEntity<List<CriteriaResponse>> getAllCriteria() {
        return ResponseEntity.ok()
                .body(criteriaService.getAllCriteria());
    }
}
