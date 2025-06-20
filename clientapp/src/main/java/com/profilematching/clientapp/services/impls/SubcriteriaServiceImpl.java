package com.profilematching.clientapp.services.impls;

import com.profilematching.clientapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.SubcriteriaResponse;
import com.profilematching.clientapp.services.SubcriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class SubcriteriaServiceImpl implements SubcriteriaService {

    @Autowired
    private RestTemplate restTemplate;

    private final String url = "http://localhost:8080/api/subcriteria";

    @Override
    public List<SubcriteriaResponse> getAllSubcriteria() {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<SubcriteriaResponse>>() {}
                )
                .getBody();
    }

    @Override
    public SubcriteriaResponse getSubcriteriaById(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.GET,
                        null,
                        SubcriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public SubcriteriaResponse addSubcriteria(AddSubcriteriaRequest addSubcriteriaRequest) {
        return restTemplate
                .exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(addSubcriteriaRequest),
                        SubcriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public SubcriteriaResponse updateSubcriteria(Integer Id, UpdateSubcriteriaRequest updateSubcriteriaRequest) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.PUT,
                        new HttpEntity<>(updateSubcriteriaRequest),
                        SubcriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public SubcriteriaResponse deleteSubcriteria(Integer Id) {
        return restTemplate
                .exchange(
                        url + "/" + Id,
                        HttpMethod.DELETE,
                        null,
                        SubcriteriaResponse.class
                )
                .getBody();
    }

    @Override
    public byte[] generateSubcriteriaPdf() throws Exception {
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
    public Long countSubcriteria() {
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