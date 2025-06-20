package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.clientapp.models.dtos.responses.RankingResponse;
import com.profilematching.clientapp.services.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/api/ranking";

    @Override
    public List<RankingResponse> getAllRankings() {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RankingResponse>>() {}
                )
                .getBody();
    }

    @Override
    public void calculateAllRankings() {
        restTemplate.exchange(
                url + "/calculate",
                HttpMethod.POST,
                null,
                Void.class
        );
    }

    @Override
    public byte[] generateRankingPdf() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url + "/export",
                HttpMethod.GET,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch PDF. Status: " + response.getStatusCode());
        }
    }
}