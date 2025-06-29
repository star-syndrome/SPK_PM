package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateResponse;
import com.profilematching.clientapp.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CandidateRestController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping(
            path = "/candidate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateResponse>> getAllCandidate() {
        return ResponseEntity.ok().body(candidateService.getAllCandidates());
    }

    @GetMapping(
            path = "/candidate/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(candidateService.getCandidateById(id));
    }

    @GetMapping(
            path = "/candidate/total",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getTotalCandidate() {
        return ResponseEntity.ok().body(candidateService.countCandidate());
    }

    @GetMapping(
            path = "/candidate/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = candidateService.generateCandidatePdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("laporan-kandidat.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(
            path = "/candidate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> addCandidate(@RequestBody AddCandidateRequest addCandidateRequest) {
        return ResponseEntity.ok().body(candidateService.addCandidate(addCandidateRequest));
    }

    @PutMapping(
            path = "/candidate/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> updateCandidate(@PathVariable Integer id, @RequestBody UpdateCandidateRequest updateCandidateRequest) {
        return ResponseEntity.ok().body(candidateService.updateCandidate(id, updateCandidateRequest));
    }

    @DeleteMapping(
            path = "/candidate/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateResponse> deleteCandidate(@PathVariable Integer id) {
        return ResponseEntity.ok().body(candidateService.deleteCandidate(id));
    }
}