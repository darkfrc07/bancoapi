package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
                any(), any(), any())).thenReturn(1);

        cuentaRepository.agregarCuenta(cuenta);

        verify(jdbcTemplate, times(1)).update(eq("INSERT INTO cuenta (numero, saldo, id_cliente) VALUES (?, ?, ?)"), 
                any(), any(), any());
    }

    @Test
    void testAgregarCuentaExcepcion() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        when(jdbcTemplate.update(eq("INSERT INTO cuenta (numero, saldo, id_cliente) VALUES (?, ?, ?)"), 
                any(), any(), any())).thenThrow(DuplicateKeyException.class);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cuentaRepository.agregarCuenta(cuenta));
        assertEquals("Error: El número de cuenta ya existe.", thrown.getMessage());
    }

    @Test
    void testActualizarCuenta() {
        when(jdbcTemplate.update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), any(), any())).thenReturn(1);

        int resultado = cuentaRepository.actualizarCuenta("12345", 5000.0);

        assertEquals(1, resultado);
        verify(jdbcTemplate, times(1)).update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), any(), any());
    }

    @Test
    void testActualizarCuentaNoEncontrada() {
        when(jdbcTemplate.update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), any(), any())).thenReturn(0);

        int resultado = cuentaRepository.actualizarCuenta("12345", 5000.0);

        assertEquals(0, resultado);
        verify(jdbcTemplate, times(1)).update(eq("UPDATE cuenta SET saldo = ? WHERE numero = ?"), any(), any());
    }

    @Test
    void testEliminarCuenta() {
        when(jdbcTemplate.update(eq("DELETE FROM cuenta WHERE numero = ?"), any(Object[].class))).thenReturn(1);

        int resultado = cuentaRepository.eliminarCuenta("12345");

        assertEquals(1, resultado);
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM cuenta WHERE numero = ?"), any(Object[].class));
    }

    @Test
    void testEliminarCuentaNoExistente() {
        when(jdbcTemplate.update(eq("DELETE FROM cuenta WHERE numero = ?"), any(Object[].class))).thenReturn(0);

        int resultado = cuentaRepository.eliminarCuenta("12345");

        assertEquals(0, resultado);
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM cuenta WHERE numero = ?"), any(Object[].class));
    }

    @Test
    void testObtenerCuentas() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        RowMapper<Cuenta> rowMapper = mock(RowMapper.class);
        when(jdbcTemplate.query(eq("SELECT * FROM cuenta"), eq(rowMapper))).thenReturn(List.of(cuenta));

        List<Cuenta> cuentas = cuentaRepository.obtenerCuentas();

        assertNotNull(cuentas);
        assertEquals(1, cuentas.size());
        assertEquals("12345", cuentas.get(0).getNumero());
    }

    @Test
    void testBuscarPorNumeroCuentaExistente() {
        Cuenta cuenta = new Cuenta("12345", 1000.0, 1L);
        RowMapper<Cuenta> rowMapper = mock(RowMapper.class);
        when(jdbcTemplate.query(eq("SELECT * FROM cuenta WHERE numero = ?"), eq(rowMapper), eq("12345"))).thenReturn(List.of(cuenta));

        Optional<Cuenta> resultado = cuentaRepository.buscarPorNumero("12345");

        assertTrue(resultado.isPresent());
        assertEquals("12345", resultado.get().getNumero());
    }

    @Test
    void testBuscarPorNumeroCuentaNoExistente() {
        RowMapper<Cuenta> rowMapper = mock(RowMapper.class);
        when(jdbcTemplate.query(eq("SELECT * FROM cuenta WHERE numero = ?"), eq(rowMapper), eq("12345"))).thenReturn(List.of());

        Optional<Cuenta> resultado = cuentaRepository.buscarPorNumero("12345");

        assertFalse(resultado.isPresent());
    }

}
