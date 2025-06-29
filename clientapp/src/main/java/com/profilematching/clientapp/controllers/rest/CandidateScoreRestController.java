package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.models.dtos.requests.BulkCandidateScoreRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.clientapp.services.CandidateScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CandidateScoreRestController {

    @Autowired
    private CandidateScoreService candidateScoreService;

    @GetMapping(
            path = "/candidate-score",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateScoreResponse>> getAllCandidateScore() {
        return ResponseEntity.ok().body(candidateScoreService.getAllCandidateScore());
    }

    @GetMapping(
            path = "/candidate-score/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> getCandidateScoreById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(candidateScoreService.getCandidateScoreById(id));
    }

    @GetMapping(
            path = "/candidate-score/candidate/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CandidateScoreResponse>> getCandidateScoreByCandidateId(@PathVariable Integer id) {
        return ResponseEntity.ok().body(candidateScoreService.getScoresByCandidateId(id));
    }

    @GetMapping(
            path = "/candidate-score/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = candidateScoreService.generateCandidateScorePdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("laporan-skor-kandidat.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(
            path = "/candidate-score",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addOrUpdateCandidateScore(@RequestBody BulkCandidateScoreRequest request) {
        return ResponseEntity.ok().body(candidateScoreService.saveOrUpdateBulkScores(request));
    }

    @DeleteMapping(
            path = "/candidate-score/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CandidateScoreResponse> deleteCandidateScore(@PathVariable Integer id) {
        return ResponseEntity.ok().body(candidateScoreService.deleteCandidateScore(id));
    }
}