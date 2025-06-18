package com.profilematching.serverapp.controllers;

import com.profilematching.serverapp.models.dtos.responses.RankingResponse;
import com.profilematching.serverapp.services.PdfService;
import com.profilematching.serverapp.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private PdfService pdfService;

    @PostMapping(
            path = "/ranking/calculate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> calculateAndSaveRankings() {
        rankingService.calculateAllRankings();
        return ResponseEntity.ok("Ranking berhasil dihitung dan disimpan.");
    }

    @GetMapping(
            path = "/ranking",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RankingResponse>> getAllRankings() {
        return ResponseEntity.ok()
                .body(rankingService.getAllRankings());
    }

    @GetMapping(
            path = "/ranking/export",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportRankingPdf() throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=laporan-peringkat.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfService.generateRankingPdf());
    }
}