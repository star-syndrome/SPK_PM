package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.requests.BulkCandidateScoreRequest;
import com.profilematching.serverapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.serverapp.services.CandidateScoreService;
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
public class CandidateScoreController {

    @Autowired
    private CandidateScoreService candidateScoreService;

    @Autowired
    private PdfService pdfService;

    @GetMapping(
            path = "/candidate-score",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateScoreResponse>> getAllCandidateScore() {
        return ResponseEntity.ok()
                .body(candidateScoreService.getAllCandidateScore());
    }

    @GetMapping(
            path = "/candidate-score/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> getCandidateScoreById(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateScoreService.getCandidateScoreById(Id));
    }

    @GetMapping(
            path = "/candidate-score/candidate/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CandidateScoreResponse>> getScoresByCandidateId(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateScoreService.getScoresByCandidateId(Id));
    }

    @GetMapping(
            path = "/candidate-score/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportCriteriaPdf() throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=laporan-skor-kandidat.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfService.generateCandidateScorePdf());
    }

    @PostMapping(
            path = "/candidate-score",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addOrUpdateCandidateScores(@Validated @RequestBody BulkCandidateScoreRequest request) {
        return ResponseEntity.ok()
                .body(candidateScoreService.saveOrUpdateBulkScores(request));
    }

    @DeleteMapping(
            path = "/candidate-score/{Id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> deleteCandidateScore(@PathVariable Integer Id) {
        return ResponseEntity.ok()
                .body(candidateScoreService.deleteCandidateScore(Id));
    }
}