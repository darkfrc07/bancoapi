package com.example.banco_app.controller;

import com.example.banco_app.model.Movimiento;
import com.example.banco_app.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MovimientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movimientoController).build();
    }

    @Test
    void testAgregarMovimiento() throws Exception {
        Movimiento movimiento = new Movimiento(null, null, null, 0, null);
        movimiento.setNumeroCuenta("123");
        movimiento.setTipo("crédito");
        movimiento.setValor(1000.0);
        movimiento.setFecha(LocalDate.parse("2024-02-16"));
        when(movimientoService.agregarMovimiento(eq("123"), any(Movimiento.class)))
                .thenReturn("Movimiento registrado");

        mockMvc.perform(MockMvcRequestBuilders.post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimiento)))
                .andExpect(status().isOk())
                .andExpect(content().string("Movimiento registrado"));
    }

    @Test
    void testObtenerTodos() throws Exception {
        List<Movimiento> movimientos = Arrays.asList(
                new Movimiento("crédito", 500.0, "2024-02-16"),
                new Movimiento("débito", -200.0, "2024-02-17")
        );

        when(movimientoService.obtenerTodos()).thenReturn(movimientos);

        mockMvc.perform(MockMvcRequestBuilders.get("/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].tipo").value("crédito"))
                .andExpect(jsonPath("$[1].tipo").value("débito"));
    }

    @Test
    void testObtenerMovimientosPorNumeroCuenta_ConResultados() throws Exception {
        List<Movimiento> movimientos = Arrays.asList(
                new Movimiento("crédito", 500.0, "2024-02-16"),
                new Movimiento("débito", -200.0, "2024-02-17")
        );

        when(movimientoService.buscarPorNumeroCuenta("123")).thenReturn(movimientos);

        mockMvc.perform(MockMvcRequestBuilders.get("/movimientos/cuenta/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testObtenerMovimientosPorNumeroCuenta_SinResultados() throws Exception {
        when(movimientoService.buscarPorNumeroCuenta("999")).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.get("/movimientos/cuenta/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"mensaje\": \"No se encontraron movimientos para la cuenta 999.\"}"));
    }

    @Test
    void testActualizarMovimiento_Exitoso() throws Exception {
        Movimiento movimientoActualizado = new Movimiento(null, null, null, 0, null);
        movimientoActualizado.setTipo("débito");
        movimientoActualizado.setValor(300.0);
        movimientoActualizado.setFecha(LocalDate.parse("2024-02-16"));
        when(movimientoService.actualizarMovimiento(eq(1L), any(Movimiento.class)))
                .thenReturn("Movimiento actualizado");

        mockMvc.perform(MockMvcRequestBuilders.put("/movimientos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimientoActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().string("Movimiento actualizado"));
    }

    @Test
    void testActualizarMovimiento_NoEncontrado() throws Exception {
        Movimiento movimientoActualizado = new Movimiento(null, null, null, 0, null);
        movimientoActualizado.setTipo("débito");
        movimientoActualizado.setValor(300.0);
        movimientoActualizado.setFecha(LocalDate.parse("2024-02-16"));
        when(movimientoService.actualizarMovimiento(eq(99L), any(Movimiento.class)))
                .thenReturn("Error: Movimiento no encontrado");

        mockMvc.perform(MockMvcRequestBuilders.put("/movimientos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimientoActualizado)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Movimiento no encontrado"));
    }
}
