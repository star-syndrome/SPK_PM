package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.responses.RankingResponse;

import java.util.List;

public interface RankingService {

    List<RankingResponse> getAllRankings();

    void calculateAllRankings();

    byte[] generateRankingPdf() throws Exception;
}