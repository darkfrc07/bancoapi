package com.example.banco_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.banco_app.service.ChuckNorrisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) // Asegura que JUnit maneje las inyecciones de Spring
@WebMvcTest(ChuckNorrisController.class) // Carga solo el controlador
public class ChuckNorrisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Aquí se usa MockBean en lugar de Mock
    private ChuckNorrisService chuckNorrisService;

    @Test
    public void testObtenerChiste() throws Exception {
        // Simulamos la respuesta del servicio
        String fakeJoke = "Chuck Norris puede hacer un hola mundo en una sola línea de código: HolaMundo.";
        when(chuckNorrisService.obtenerChisteChuckNorris()).thenReturn(fakeJoke);

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/chucknorris"))
                .andExpect(status().isOk())  // Verifica que la respuesta sea 200 OK
                .andExpect(content().string(fakeJoke));  // Verifica el contenido

        // Verificamos que el servicio se llamó una vez
        verify(chuckNorrisService, times(1)).obtenerChisteChuckNorris();
    }
}
