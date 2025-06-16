package com.profilematching.serverapp.services;

import com.profilematching.serverapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.serverapp.models.dtos.responses.SubcriteriaResponse;

import java.util.List;

public interface SubcriteriaService {

    List<SubcriteriaResponse> getAllSubcriteria();

    SubcriteriaResponse getSubcriteriaById(Integer Id);

    SubcriteriaResponse addSubcriteria(AddSubcriteriaRequest addSubcriteriaRequest);

    SubcriteriaResponse updateSubcriteria(Integer Id, UpdateSubcriteriaRequest updateSubcriteriaRequest);

    SubcriteriaResponse deleteSubcriteria(Integer Id);

    Long countSubcriteria();
}