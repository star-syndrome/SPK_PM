package com.profilematching.clientapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/kriteria")
    public String criteriaView(){
        return "pages/admin/criteria";
    }

    @GetMapping("/subkriteria")
    public String subcriteriaView(){
        return "pages/admin/subcriteria";
    }

    @GetMapping("/kader")
    public String candidateView(){
        return "pages/admin/candidate";
    }

    @GetMapping("/penilaian")
    public String candidateScoreView(){
        return "pages/admin/candidate-score";
    }

    @GetMapping("/perhitungan")
    public String ScoringView(){
        return "pages/admin/scoring";
    }

    @GetMapping("/peringkat")
    public String rankingView(){
        return "pages/admin/ranking";
    }
}