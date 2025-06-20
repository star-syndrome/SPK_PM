package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.requests.AddCandidateRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCandidateRequest;
import com.profilematching.clientapp.models.dtos.responses.CandidateResponse;
import com.profilematching.clientapp.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/api/candidate";

    @Override
    public List<CandidateResponse> getAllCandidates() {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CandidateResponse>>() {}
                )
                .getBody();
    }

    @Override
    public CandidateResponse getCandidateById(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.GET,
                        null,
                        CandidateResponse.class
                )
                .getBody();
    }

    @Override
    public CandidateResponse addCandidate(AddCandidateRequest addCandidateRequest) {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(addCandidateRequest),
                        CandidateResponse.class
                )
                .getBody();
    }

    @Override
    public CandidateResponse updateCandidate(Integer Id, UpdateCandidateRequest updateCandidateRequest) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.PUT,
                        new HttpEntity<>(updateCandidateRequest),
                        CandidateResponse.class
                )
                .getBody();
    }

    @Override
    public CandidateResponse deleteCandidate(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.DELETE,
                        null,
                        CandidateResponse.class
                )
                .getBody();
    }

    @Override
    public byte[] generateCandidatePdf() throws Exception {
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

    @Override
    public Long countCandidate() {
        return restTemplate
                .exchange(
                        url + "/total",
                        HttpMethod.GET,
                        null,
                        Long.class
                )
                .getBody();
    }
}