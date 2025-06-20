package com.profilematching.clientapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(
            path = "/home"
    )
    public String dashboard(Model model) {
        model.addAttribute("isActive", "home");
        return "dashboard";
    }
}