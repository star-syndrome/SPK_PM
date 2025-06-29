package com.profilematching.clientapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/criteria")
    public String criteriaView(){
        return "pages/admin/criteria";
    }

    @GetMapping("/subcriteria")
    public String subcriteriaView(){
        return "pages/admin/subcriteria";
    }

    @GetMapping("/candidate")
    public String candidateView(){
        return "pages/admin/candidate";
    }

    @GetMapping("/candidate-score")
    public String candidateScoreView(){
        return "pages/admin/candidate-score";
    }
}