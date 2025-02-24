package com.example.banco_app.repository;

import com.example.banco_app.model.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoRepositoryTest {
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MovimientoRepository movimientoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarMovimiento() {
        Movimiento movimiento = new Movimiento(null, "DEBITO", LocalDate.now(), 500.0, "12345");
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            keyHolder.getKeyList().add(java.util.Map.of("id", 1L)); // Simular ID generado
            return 1;
        });

        Long id = movimientoRepository.agregarMovimiento("12345", movimiento);

        assertNotNull(id);
        assertEquals(1L, id);
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    void testObtenerMovimientos() {
        Movimiento movimiento = new Movimiento(1L, "CREDITO", LocalDate.now(), 1000.0, "12345");
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
            .thenReturn(List.of(movimiento));

        List<Movimiento> movimientos = movimientoRepository.obtenerMovimientos();

        assertFalse(movimientos.isEmpty());
        assertEquals("CREDITO", movimientos.get(0).getTipo());
    }

    @Test
    void testBuscarPorNumeroCuenta() {
        Movimiento movimiento = new Movimiento(1L, "DEBITO", LocalDate.now(), 200.0, "12345");
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq("12345")))
            .thenReturn(List.of(movimiento));

        List<Movimiento> movimientos = movimientoRepository.buscarPorNumeroCuenta("12345");

        assertEquals(1, movimientos.size());
        assertEquals("12345", movimientos.get(0).getNumeroCuenta());
    }

    @Test
    void testBuscarPorId_Encontrado() {
        Movimiento movimiento = new Movimiento(1L, "DEBITO", LocalDate.now(), 300.0, "12345");

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L)))
            .thenReturn(List.of(movimiento));

        Optional<Movimiento> resultado = movimientoRepository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(99L)))
            .thenReturn(List.of());

        Optional<Movimiento> resultado = movimientoRepository.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testActualizarMovimiento() {
        Movimiento movimiento = new Movimiento(1L, "CREDITO", LocalDate.now(), 150.0, "12345");

        when(jdbcTemplate.update(anyString(), any(), any(), any(), any()))
            .thenReturn(1);

        int filasActualizadas = movimientoRepository.actualizarMovimiento(1L, movimiento);

        assertEquals(1, filasActualizadas);
        verify(jdbcTemplate, times(1)).update(anyString(), any(), any(), any(), any());
    }
}
