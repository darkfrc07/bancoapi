package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.CuentaRepository.CuentaRowMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
        when(jdbcTemplate.update(eq("INSERT INTO cuenta (numero, saldo, id_cliente) VALUES (?, ?, ?)"),
                eq("12345"), eq(1000.0), eq(1L))).thenReturn(1);

        cuentaRepository.agregarCuenta(cuenta);

        verify(jdbcTemplate, times(1)).update(anyString(), eq("12345"), eq(1000.0), eq(1L));
    }

    @Test
    void testAgregarCuentaExcepcion() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        when(jdbcTemplate.update(anyString(), any(), any(), any()))
                .thenThrow(new DuplicateKeyException("Error: El número de cuenta ya existe."));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cuentaRepository.agregarCuenta(cuenta));
        assertTrue(thrown.getMessage().contains("Error: El número de cuenta ya existe."));
    }


    @Test
    void testActualizarCuenta() {
        when(jdbcTemplate.update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), eq(5000.0), eq("12345"))).thenReturn(1);

        int resultado = cuentaRepository.actualizarCuenta("12345", 5000.0);

        assertEquals(1, resultado);
        verify(jdbcTemplate, times(1)).update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), eq(5000.0), eq("12345"));
    }

    @Test
    void testActualizarCuentaNoEncontrada() {
        when(jdbcTemplate.update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), eq(5000.0), eq("12345"))).thenReturn(0);

        int resultado = cuentaRepository.actualizarCuenta("12345", 5000.0);

        assertEquals(0, resultado);
    }

    @Test
    void testEliminarCuenta() {
        when(jdbcTemplate.update(anyString(), eq("12345"))).thenReturn(1);

        int resultado = cuentaRepository.eliminarCuenta("12345");

        assertEquals(1, resultado);
    }

    @Test
    void testEliminarCuentaNoExistente() {
        when(jdbcTemplate.update(anyString(), eq("12345"))).thenReturn(0);

        int resultado = cuentaRepository.eliminarCuenta("12345");

        assertEquals(0, resultado);
    }

    @Test
    void testObtenerCuentas() {
        List<Cuenta> cuentasMock = List.of(new Cuenta("12345", 5000.0, 1L));
        
        when(jdbcTemplate.query(eq("SELECT numero, saldo, id_cliente FROM cuenta"), any(CuentaRowMapper.class)))
            .thenReturn(cuentasMock);

        List<Cuenta> resultado = cuentaRepository.obtenerCuentas();

        assertEquals(1, resultado.size());
    }


    @Test
    void testBuscarPorNumeroCuentaExistente() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        when(jdbcTemplate.query(eq("SELECT numero, saldo, id_cliente FROM cuenta WHERE numero = ?"), 
                any(CuentaRowMapper.class), eq("12345")))
            .thenReturn(List.of(cuenta));

        Optional<Cuenta> resultado = cuentaRepository.buscarPorNumero("12345");

        assertTrue(resultado.isPresent());
        assertEquals("12345", resultado.get().getNumero());
    }

    @Test
    void testBuscarPorNumeroCuentaNoExistente() {
        when(jdbcTemplate.query(eq("SELECT * FROM cuenta WHERE numero = ?"), ArgumentMatchers.<RowMapper<Cuenta>>any(), eq("12345")))
                .thenReturn(List.of());

        Optional<Cuenta> resultado = cuentaRepository.buscarPorNumero("12345");

        assertFalse(resultado.isPresent());
    }
}
