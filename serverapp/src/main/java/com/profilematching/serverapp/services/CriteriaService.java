package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.requests.AddCriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateCriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.CriteriaResponse;

import java.util.List;

public interface CriteriaService {

    List<CriteriaResponse> getAllCriteria();

    CriteriaResponse getCriteriaById(Integer Id);

    CriteriaResponse addCriteria(AddCriteriaRequest addCriteriaRequest);

    CriteriaResponse updateCriteria(Integer Id, UpdateCriteriaRequest updateCriteriaRequest);

    CriteriaResponse deleteCriteria(Integer Id);

    Long countCriteria();
}