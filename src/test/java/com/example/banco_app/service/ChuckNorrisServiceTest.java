package com.example.banco_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ChuckNorrisServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ChuckNorrisService chuckNorrisService;

    @Test
    public void testObtenerChisteChuckNorris() {
        String fakeJoke = "{ \"value\": \"Chuck Norris puede dividir entre cero.\" }";
        String apiUrl = "https://api.chucknorris.io/jokes/random";

        when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(fakeJoke);

        String resultado = chuckNorrisService.obtenerChisteChuckNorris();

        assertEquals(fakeJoke, resultado);

        verify(restTemplate, times(1)).getForObject(apiUrl, String.class);
    }
}
