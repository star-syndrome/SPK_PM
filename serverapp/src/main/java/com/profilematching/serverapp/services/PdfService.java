package com.profilematching.serverapp.services;

public interface PdfService {

    byte[] generateCriteriaPdf() throws Exception;

    byte[] generateSubcriteriaPdf() throws Exception;

    byte[] generateCandidatePdf() throws Exception;

    byte[] generateCandidateScorePdf() throws Exception;

    byte[] generateRankingPdf() throws Exception;
}