package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.clientapp.services.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CriteriaRestController {

    @Autowired
    private CriteriaService criteriaService;

    @GetMapping(
            path = "/criteria",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CriteriaResponse>> getAllCriteria() {
        return ResponseEntity.ok().body(criteriaService.getAllCriteria());
    }

    @GetMapping(
            path = "/criteria/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> getCriteriaById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(criteriaService.getCriteriaById(id));
    }

    @GetMapping(
            path = "/criteria/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTotalCourses() {
        return ResponseEntity.ok().body(criteriaService.countCriteria());
    }

    @GetMapping(
            path = "/criteria/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = criteriaService.generateCriteriaPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("laporan-kriteria.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(
            path = "/criteria",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> addCriteria(@RequestBody AddCriteriaRequest addCriteriaRequest) {
        return ResponseEntity.ok().body(criteriaService.addCriteria(addCriteriaRequest));
    }

    @PutMapping(
            path = "/criteria/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> updateCriteria(@PathVariable Integer id, @RequestBody UpdateCriteriaRequest updateCriteriaRequest) {
        return ResponseEntity.ok().body(criteriaService.updateCriteria(id, updateCriteriaRequest));
    }

    @DeleteMapping(
            path = "/criteria/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CriteriaResponse> deleteCategory(@PathVariable Integer id) {
        return ResponseEntity.ok().body(criteriaService.deleteCriteria(id));
    }
}