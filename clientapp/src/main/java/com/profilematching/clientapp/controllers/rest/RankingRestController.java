package com.profilematching.clientapp.controllers.rest;

import com.profilematching.clientapp.models.dtos.responses.RankingResponse;
import com.profilematching.clientapp.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RankingRestController {

    @Autowired
    private RankingService rankingService;

    @GetMapping(
            path = "/ranking",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<RankingResponse>> getAllRankings() {
        return ResponseEntity.ok().body(rankingService.getAllRankings());
    }

    @PostMapping(
            path = "/ranking/calculate")
    public ResponseEntity<Void> calculateAllRankings() {
        rankingService.calculateAllRankings();
        return ResponseEntity.ok().build();
    }

    @GetMapping(
            path = "/ranking/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf() {
        try {
            byte[] pdfBytes = rankingService.generateRankingPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("laporan-peringkat.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}