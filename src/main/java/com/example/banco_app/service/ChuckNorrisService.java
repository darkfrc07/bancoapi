package com.example.banco_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChuckNorrisService {

    private final RestTemplate restTemplate;

    @Autowired
    public ChuckNorrisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerChisteChuckNorris() {
        String url = "https://api.chucknorris.io/jokes/random";
        return restTemplate.getForObject(url, String.class);  // Obtener el chiste
    }
}

