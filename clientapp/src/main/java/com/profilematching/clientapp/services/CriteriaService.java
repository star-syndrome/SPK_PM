package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.CriteriaResponse;

import java.util.List;

public interface CriteriaService {

    List<CriteriaResponse> getAllCriteria();

    CriteriaResponse getCriteriaById(Integer Id);

    CriteriaResponse addCriteria(AddCriteriaRequest addCriteriaRequest);

    CriteriaResponse updateCriteria(Integer Id, UpdateCriteriaRequest updateCriteriaRequest);

    CriteriaResponse deleteCriteria(Integer Id);

    byte[] generateCriteriaPdf() throws Exception;

    Long countCriteria();
}