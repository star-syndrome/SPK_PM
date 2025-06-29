package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateResponse;
import com.profilematching.serverapp.services.CandidateService;
import com.profilematching.serverapp.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private PdfService pdfService;

    @GetMapping(
            path = "/candidate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateResponse>> getAllCandidate() {
        return ResponseEntity.ok()
                .body(candidateService.getAllCandidates());
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

    @GetMapping(
            path = "/candidate/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportCriteriaPdf() throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=laporan-kandidat.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfService.generateCandidatePdf());
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