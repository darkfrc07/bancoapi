package com.example.banco_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.repository.MovimientoRepository;

class MovimientoServiceTest {
    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarMovimiento_CuentaNoExiste() {
        when(cuentaService.obtenerCuentaPorNumero("12345")).thenReturn(Optional.empty());

        String resultado = movimientoService.actualizarMovimiento("12345", new Movimiento("DEBITO", 50.0, null));

        assertEquals("Error: Cuenta no encontrada.", resultado);
    }

    @Test
    void testActualizarMovimiento_DepositoExitoso() {
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("DEBITO", 50.0, null);

        when(cuentaService.obtenerCuentaPorNumero("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaService.actualizarSaldo("12345", 150.0))
            .thenReturn(new ResponseEntity<>("Saldo actualizado", HttpStatus.OK));
        when(movimientoRepository.agregarMovimiento(anyString(), any(Movimiento.class))).thenReturn(1L);

        String resultado = movimientoService.actualizarMovimiento("12345", movimiento);

        assertEquals("Movimiento registrado con éxito. ID: 1", resultado);
        verify(cuentaService).actualizarSaldo("12345", 150.0);
        verify(movimientoRepository).agregarMovimiento(eq("12345"), any(Movimiento.class));
    }

    @Test
    void testActualizarMovimiento_RetiroExitoso() {
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("CREDITO", 50.0, null);

        when(cuentaService.obtenerCuentaPorNumero("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaService.actualizarSaldo("12345", 50.0))
            .thenReturn(new ResponseEntity<>("Saldo actualizado", HttpStatus.OK));
        when(movimientoRepository.agregarMovimiento(anyString(), any(Movimiento.class))).thenReturn(2L);

        String resultado = movimientoService.actualizarMovimiento("12345", movimiento);

        assertEquals("Movimiento registrado con éxito. ID: 2", resultado);
        verify(cuentaService).actualizarSaldo("12345", 50.0);
        verify(movimientoRepository).agregarMovimiento(eq("12345"), any(Movimiento.class));
    }

    @Test
    void testActualizarMovimiento_SaldoInsuficiente() {
        Cuenta cuenta = new Cuenta("12345", 40.0, 1L);
        Movimiento movimiento = new Movimiento("CREDITO", 50.0, null);

        when(cuentaService.obtenerCuentaPorNumero("12345")).thenReturn(Optional.of(cuenta));

        String resultado = movimientoService.actualizarMovimiento("12345", movimiento);

        assertEquals("Error: Saldo insuficiente.", resultado);
        verify(cuentaService, never()).actualizarSaldo(anyString(), anyDouble());
        verify(movimientoRepository, never()).agregarMovimiento(anyString(), any(Movimiento.class));
    }

    @Test
    void testActualizarMovimiento_TipoInvalido() {
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("INVALIDO", 30.0, null); // Tipo inválido

        when(cuentaService.obtenerCuentaPorNumero("12345")).thenReturn(Optional.of(cuenta));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.actualizarMovimiento("12345", movimiento);
        });

        assertEquals("Tipo de movimiento inválido: INVALIDO", exception.getMessage());
    }

    @Test
    void testBuscarPorNumeroCuenta() {
        String numeroCuenta = "12345";
        List<Movimiento> movimientos = List.of(
            new Movimiento("DEBITO", 100.0, "12345"),  
            new Movimiento("CREDITO", 50.0, "12345")   
        );

        when(movimientoRepository.buscarPorNumeroCuenta(numeroCuenta)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.buscarPorNumeroCuenta(numeroCuenta);

        assertEquals(2, resultado.size());
        assertEquals(100.0, resultado.get(0).getValor());  // Corregido
        assertEquals(50.0, resultado.get(1).getValor());   // Corregido
    }
}
