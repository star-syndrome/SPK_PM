package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.services.SubcriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subcriteria")
public class SubcriteriaRestController {

    @Autowired
    private SubcriteriaService subcriteriaService;

    @GetMapping(
            path = "/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTotalCourses() {
        return ResponseEntity.ok().body(subcriteriaService.countSubcriteria());
    }
}