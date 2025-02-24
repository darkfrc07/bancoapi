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
        // Simulamos la respuesta de la API
        String fakeJoke = "{ \"value\": \"Chuck Norris puede dividir entre cero.\" }";
        String apiUrl = "https://api.chucknorris.io/jokes/random";

        // Simulamos que cuando se llame a RestTemplate, devuelva el chiste simulado
        when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(fakeJoke);

        // Ejecutamos el método
        String resultado = chuckNorrisService.obtenerChisteChuckNorris();

        // Verificamos que el resultado es el esperado
        assertEquals(fakeJoke, resultado);

        // Aseguramos que se llamó a la API una vez
        verify(restTemplate, times(1)).getForObject(apiUrl, String.class);
    }
}
