package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.SubcriteriaResponse;
import com.profilematching.serverapp.services.SubcriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class SubcriteriaController {

    @Autowired
    private SubcriteriaService subcriteriaService;

    @GetMapping(
            path = "/subcriteria",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<SubcriteriaResponse>> getAllSubcriteria() {
        return ResponseEntity.ok()
                .body(subcriteriaService.getAllSubcriteria());
    }

    @GetMapping(
            path = "/subcriteria/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> getSubcriteriaById(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(subcriteriaService.getSubcriteriaById(Id));
    }

    @GetMapping(
            path = "/subcriteria/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> countSubcriteria() {
        return ResponseEntity.ok()
                .body(subcriteriaService.countSubcriteria());
    }

    @PostMapping(
            path = "/subcriteria",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> addCriteria(@Validated @RequestBody AddSubcriteriaRequest addSubcriteriaRequest){
        return ResponseEntity.ok()
                .body(subcriteriaService.addSubcriteria(addSubcriteriaRequest));
    }

    @PutMapping(
            path = "/subcriteria/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> updateSubcriteria(
            @PathVariable Integer Id, @Validated @RequestBody UpdateSubcriteriaRequest updateSubcriteriaRequest
    ){
        return ResponseEntity.ok()
                .body(subcriteriaService.updateSubcriteria(Id, updateSubcriteriaRequest));
    }

    @DeleteMapping(
            path = "/subcriteria/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> deleteSubcriteria(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(subcriteriaService.deleteSubcriteria(Id));
    }
}