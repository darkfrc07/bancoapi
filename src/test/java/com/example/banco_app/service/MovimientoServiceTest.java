package com.example.banco_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.repository.CuentaRepository;
import com.example.banco_app.repository.MovimientoRepository;

class MovimientoServiceTest {
    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarMovimiento_CuentaNoExiste() {
        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.empty());

        String resultado = movimientoService.agregarMovimiento("12345", new Movimiento("débito", 50.0, null));

        assertEquals("Error: Cuenta no encontrada.", resultado);
    }

    @Test
    void testAgregarMovimiento_DepositoExitoso() { 
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("débito", 50.0, null);
        
        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.actualizarCuenta("12345", 150.0)).thenReturn(1); 
        when(movimientoRepository.agregarMovimiento(anyString(), any(Movimiento.class))).thenReturn(1L);

        String resultado = movimientoService.agregarMovimiento("12345", movimiento);

        assertEquals("Movimiento registrado con éxito. ID: 1", resultado);
        verify(cuentaRepository).actualizarCuenta("12345", 150.0);  
        verify(movimientoRepository).agregarMovimiento(eq("12345"), any(Movimiento.class)); 
    }

    @Test
    void testAgregarMovimiento_RetiroExitoso() { 
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("crédito", 50.0, "12345");

        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.actualizarCuenta("12345", 50.0)).thenReturn(1); 
        when(movimientoRepository.agregarMovimiento(anyString(), any(Movimiento.class))).thenReturn(2L);

        String resultado = movimientoService.agregarMovimiento("12345", movimiento);

        assertEquals("Movimiento registrado con éxito. ID: 2", resultado);
        verify(cuentaRepository).actualizarCuenta("12345", 50.0);
        verify(movimientoRepository).agregarMovimiento(eq("12345"), any(Movimiento.class));
    }

    @Test
    void testAgregarMovimiento_SaldoInsuficiente() { 
        Cuenta cuenta = new Cuenta("12345", 40.0, 1L);
        Movimiento movimiento = new Movimiento("crédito", 50.0, null);

        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(cuenta));

        String resultado = movimientoService.agregarMovimiento("12345", movimiento);

        assertEquals("Error: Saldo insuficiente.", resultado);
        verify(cuentaRepository, never()).actualizarCuenta(anyString(), anyDouble()); 
        verify(movimientoRepository, never()).agregarMovimiento(anyString(), any(Movimiento.class)); 
    }

    @Test
    void testAgregarMovimiento_TipoInvalido() { 
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento("credito", 30.0, null); 

        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(cuenta));

        String resultado = movimientoService.agregarMovimiento("12345", movimiento);

        assertEquals("Error: Tipo de movimiento inválido.", resultado);
        verify(cuentaRepository, never()).actualizarCuenta(anyString(), anyDouble()); 
        verify(movimientoRepository, never()).agregarMovimiento(anyString(), any(Movimiento.class)); 
    }

    @Test
    void testAgregarMovimiento_AsignarFechaSiEsNula() {
        Cuenta cuenta = new Cuenta("12345", 100.0, 1L);
        Movimiento movimiento = new Movimiento(null, "débito", null, 20.0, null);
        movimiento.setFecha(null); 
        when(cuentaRepository.buscarPorNumero("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.actualizarCuenta("12345", 120.0)).thenReturn(1);
        when(movimientoRepository.agregarMovimiento(anyString(), any(Movimiento.class))).thenReturn(3L);

        String resultado = movimientoService.agregarMovimiento("12345", movimiento);

        assertEquals("Movimiento registrado con éxito. ID: 3", resultado);
        assertNotNull(movimiento.getFecha()); 
        assertEquals(LocalDate.now(), movimiento.getFecha()); 
    }
}
