package com.example.banco_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banco_app.service.ChuckNorrisService;

@RestController
public class ChuckNorrisController {

    private final ChuckNorrisService chuckNorrisService;

    public ChuckNorrisController(ChuckNorrisService chuckNorrisService) {
        this.chuckNorrisService = chuckNorrisService;
    }

    @GetMapping("/chucknorris")
    public String obtenerChiste() {
        return chuckNorrisService.obtenerChisteChuckNorris();
    }
}

