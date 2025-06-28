package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.SubcriteriaResponse;
import com.profilematching.clientapp.services.SubcriteriaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SubcriteriaRestController {

    @Autowired
    private SubcriteriaService subcriteriaService;

    @GetMapping(
            path = "/subcriteria",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<SubcriteriaResponse>> getAllSubriteria() {
        return ResponseEntity.ok().body(subcriteriaService.getAllSubcriteria());
    }

    @GetMapping(
            path = "/subcriteria/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> getSubcriteriaById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(subcriteriaService.getSubcriteriaById(id));
    }

    @GetMapping(
            path = "/subcriteria/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTotalSubcriteria() {
        return ResponseEntity.ok().body(subcriteriaService.countSubcriteria());
    }

    @GetMapping(
            path = "/subcriteria/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = subcriteriaService.generateSubcriteriaPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("laporan-subkriteria.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(
            path = "/subcriteria",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> addSubcriteria(@RequestBody AddSubcriteriaRequest addSubcriteriaRequest) {
        return ResponseEntity.ok().body(subcriteriaService.addSubcriteria(addSubcriteriaRequest));
    }

    @PutMapping(
            path = "/subcriteria/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> updateSubcriteria(@PathVariable Integer id, @RequestBody UpdateSubcriteriaRequest updateSubcriteriaRequest) {
        return ResponseEntity.ok().body(subcriteriaService.updateSubcriteria(id, updateSubcriteriaRequest));
    }

    @DeleteMapping(
            path = "/subcriteria/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubcriteriaResponse> deleteSubcriteria(@PathVariable Integer id) {
        return ResponseEntity.ok().body(subcriteriaService.deleteSubcriteria(id));
    }
}