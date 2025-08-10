package com.profilematching.clientapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(
            path = "/beranda"
    )
    public String dashboard() {
        return "dashboard";
    }
}