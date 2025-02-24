package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaRepositoryTest {
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarCuenta() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any())).thenReturn(1);

        cuentaRepository.agregarCuenta(cuenta);

        verify(jdbcTemplate, times(1)).update(anyString(), any(), any(), any());
    }

    @Test
    void testActualizarCuenta() {
        when(jdbcTemplate.update(anyString(), any(), any())).thenReturn(1);

        int resultado = cuentaRepository.actualizarCuenta("12345", 5000.0);

        assertEquals(1, resultado);
        verify(jdbcTemplate, times(1)).update(anyString(), any(), any());
    }
}
