package com.profilematching.serverapp.services.impls;

import com.profilematching.serverapp.models.dtos.responses.*;
import com.profilematching.serverapp.services.*;
import com.profilematching.serverapp.util.PdfGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final TemplateEngine templateEngine;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final RankingService rankingService;
    private final CriteriaService criteriaService;
    private final SubcriteriaService subcriteriaService;
    private final CandidateService candidateService;
    private final CandidateScoreService candidateScoreService;


    @Override
    public byte[] generateCriteriaPdf() throws Exception {
        List<CriteriaResponse> criteriaList = criteriaService.getAllCriteria();

        Context context = new Context();
        context.setVariable("criteriaList", criteriaList);

        String htmlContent = templateEngine.process("criteria-report", context);
        return pdfGeneratorUtil.generatePdfFromHtml(htmlContent);
    }

    @Override
    public byte[] generateSubcriteriaPdf() throws Exception {
        List<SubcriteriaResponse> allSubcriteria = subcriteriaService.getAllSubcriteria();
        List<CriteriaResponse> allCriteria = criteriaService.getAllCriteria();

        Map<String, CriteriaResponse> criteriaMap = allCriteria.stream()
                .collect(Collectors.toMap(CriteriaResponse::getName, c -> c));

        Map<CriteriaResponse, List<SubcriteriaResponse>> grouped = allSubcriteria.stream()
                .collect(Collectors.groupingBy(sub -> criteriaMap.get(sub.getCriteriaName())));

        Context context = new Context();
        context.setVariable("groupedSubcriteria", grouped);

        String htmlContent = templateEngine.process("subcriteria-report", context);
        return pdfGeneratorUtil.generatePdfFromHtml(htmlContent);
    }

    @Override
    public byte[] generateCandidatePdf() throws Exception {
        List<CandidateResponse> candidates = candidateService.getAllCandidates();

        Context context = new Context();
        context.setVariable("candidates", candidates);

        String htmlContent = templateEngine.process("candidate-report", context);
        return pdfGeneratorUtil.generatePdfFromHtml(htmlContent);
    }

    @Override
    public byte[] generateCandidateScorePdf() throws Exception {
        List<CandidateScoreResponse> allScores = candidateScoreService.getAllCandidateScore();

        Map<String, List<CandidateScoreResponse>> grouped = allScores.stream()
                .collect(Collectors.groupingBy(CandidateScoreResponse::getCandidateName));

        Context context = new Context();
        context.setVariable("groupedScores", grouped);

        String htmlContent = templateEngine.process("candidate-score-report", context);
        return pdfGeneratorUtil.generatePdfFromHtml(htmlContent);
    }

    @Override
    public byte[] generateRankingPdf() throws Exception{
        List<RankingResponse> rankings = rankingService.getAllRankings();

        Context context = new Context();
        context.setVariable("rankings", rankings);

        String htmlContent = templateEngine.process("ranking-report", context);
        return pdfGeneratorUtil.generatePdfFromHtml(htmlContent);
    }
}