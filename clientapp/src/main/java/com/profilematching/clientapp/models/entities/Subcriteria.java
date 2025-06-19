package com.profilematching.clientapp.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcriteria {

    private Integer id;
    private String code;
    private String description;
    private String type;
    private Double target;
    private Criteria criteria;
}