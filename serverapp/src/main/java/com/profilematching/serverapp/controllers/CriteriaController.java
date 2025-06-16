package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.serverapp.services.CriteriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<CriteriaResponse>> getAllCriteria() {
        return ResponseEntity.ok()
                .body(criteriaService.getAllCriteria());
    }

    @GetMapping(
            path = "/criteria/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> getCriteriaById(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(criteriaService.getCriteriaById(Id));
    }

    @GetMapping(
            path = "/criteria/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> countCriteria() {
        return ResponseEntity.ok()
                .body(criteriaService.countCriteria());
    }

    @PostMapping(
            path = "/criteria",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> addCriteria(@Validated @RequestBody AddCriteriaRequest addCriteriaRequest){
        return ResponseEntity.ok()
                .body(criteriaService.addCriteria(addCriteriaRequest));
    }

    @PutMapping(
            path = "/criteria/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> updateCriteria(
            @PathVariable Integer Id, @Validated @RequestBody UpdateCriteriaRequest updateCriteriaRequest
    ){
        return ResponseEntity.ok()
                .body(criteriaService.updateCriteria(Id, updateCriteriaRequest));
    }

    @DeleteMapping(
            path = "/criteria/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> deleteCriteria(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(criteriaService.deleteCriteria(Id));
    }
}