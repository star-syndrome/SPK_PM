package com.profilematching.clientapp.services;

import com.profilematching.clientapp.models.dtos.requests.AddSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.requests.UpdateSubcriteriaRequest;
import com.profilematching.clientapp.models.dtos.responses.SubcriteriaResponse;

import java.util.List;

public interface SubcriteriaService {

    List<SubcriteriaResponse> getAllSubcriteria();

    SubcriteriaResponse getSubcriteriaById(Integer Id);

    SubcriteriaResponse addSubcriteria(AddSubcriteriaRequest addSubcriteriaRequest);

    SubcriteriaResponse updateSubcriteria(Integer Id, UpdateSubcriteriaRequest updateSubcriteriaRequest);

    SubcriteriaResponse deleteSubcriteria(Integer Id);

    byte[] generateSubcriteriaPdf() throws Exception;

    Long countSubcriteria();
}