package com.example.banco_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

class CuentaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cuentaController).build();
    }

    @Test
    void testObtenerCuentas() throws Exception {
        Cuenta cuenta1 = new Cuenta("123", 1000.0, 1L);
        Cuenta cuenta2 = new Cuenta("456", 2000.0, 2L);
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(Arrays.asList(cuenta1, cuenta2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(2))
               .andExpect(jsonPath("$[0].numero").value("123"));
    }

    @Test
    void testObtenerCuenta_Existe() throws Exception {
        Cuenta cuenta = new Cuenta("123", 1000.0, 1L);
        when(cuentaService.obtenerCuentaPorNumero("123")).thenReturn(Optional.of(cuenta));

        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas/123"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.numero").value("123"));
    }

    @Test
    void testObtenerCuenta_NoExiste() throws Exception {
        when(cuentaService.obtenerCuentaPorNumero("999")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/cuentas/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCrearCuenta() throws Exception {
        Cuenta cuenta = new Cuenta("456", 2000.0, 1L);
        when(cuentaService.crearCuenta(any(Cuenta.class))).thenReturn(ResponseEntity.ok("Cuenta creada exitosamente"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
               .andExpect(status().isOk())
               .andExpect(content().string("Cuenta creada exitosamente"));
    }

    @Test
    void testActualizarSaldo_Exito() throws Exception {
        when(cuentaService.actualizarSaldo("123", 5000.0)).thenReturn(ResponseEntity.ok("Saldo actualizado con éxito."));

        mockMvc.perform(MockMvcRequestBuilders.put("/cuentas/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Cuenta("123", 5000.0, 1L))))
               .andExpect(status().isOk())
               .andExpect(content().string("Saldo actualizado con éxito."));
    }

    @Test
    void testActualizarSaldo_Fallo() throws Exception {
        when(cuentaService.actualizarSaldo("999", 5000.0)).thenReturn(ResponseEntity.status(404).body("Error: Cuenta no encontrada."));

        mockMvc.perform(MockMvcRequestBuilders.put("/cuentas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Cuenta("999", 5000.0, 1L))))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Error: Cuenta no encontrada."));
    }

    @Test
    void testEliminarCuenta_Exito() throws Exception {
        when(cuentaService.eliminarCuenta("123")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cuentas/123"))
               .andExpect(status().isOk())
               .andExpect(content().string("Cuenta eliminada correctamente."));
    }

    @Test
    void testEliminarCuenta_Fallo() throws Exception {
        when(cuentaService.eliminarCuenta("999")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cuentas/999"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Error: Cuenta no encontrada."));
    }
}
