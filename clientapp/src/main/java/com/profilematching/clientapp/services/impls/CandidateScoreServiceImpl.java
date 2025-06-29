package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.requests.BulkCandidateScoreRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateScoreResponse;
import com.profilematching.clientapp.services.CandidateScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CandidateScoreServiceImpl implements CandidateScoreService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/api/candidate-score";

    @Override
    public List<CandidateScoreResponse> getAllCandidateScore() {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CandidateScoreResponse>>() {}
                )
                .getBody();
    }

    @Override
    public List<CandidateScoreResponse> getScoresByCandidateId(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/candidate/" + Id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CandidateScoreResponse>>() {}
                )
                .getBody();
    }

    @Override
    public CandidateScoreResponse getCandidateScoreById(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.GET,
                        null,
                        CandidateScoreResponse.class
                )
                .getBody();
    }

    @Override
    public String saveOrUpdateBulkScores(BulkCandidateScoreRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BulkCandidateScoreRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    @Override
    public CandidateScoreResponse deleteCandidateScore(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.DELETE,
                        null,
                        CandidateScoreResponse.class
                )
                .getBody();
    }

    @Override
    public byte[] generateCandidateScorePdf() throws Exception {
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