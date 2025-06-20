package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.CriteriaResponse;
import com.profilematching.clientapp.services.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CriteriaServiceImpl implements CriteriaService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/api/criteria";

    @Override
    public List<CriteriaResponse> getAllCriteria() {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CriteriaResponse>>() {}
                )
                .getBody();
    }

    @Override
    public CriteriaResponse getCriteriaById(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.GET,
                        null,
                        CriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public CriteriaResponse addCriteria(AddCriteriaRequest addCriteriaRequest) {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(addCriteriaRequest),
                        CriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public CriteriaResponse updateCriteria(Integer Id, UpdateCriteriaRequest updateCriteriaRequest) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.PUT,
                        new HttpEntity<>(updateCriteriaRequest),
                        CriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public CriteriaResponse deleteCriteria(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.DELETE,
                        null,
                        CriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public byte[] generateCriteriaPdf() throws Exception {
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
    public Long countCriteria() {
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