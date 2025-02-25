package com.example.banco_app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.CuentaRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta("123456", 500.0, 1L);
    }

    @Test
    void testCrearCuenta_Exito() {
        when(cuentaRepository.buscarPorNumero("123456")).thenReturn(Optional.empty());

        var response = cuentaService.crearCuenta(cuenta);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Cuenta creada exitosamente.", response.getBody());
        verify(cuentaRepository, times(1)).agregarCuenta(cuenta);
    }

    @Test
    void testCrearCuenta_FalloPorCuentaExistente() {
        when(cuentaRepository.buscarPorNumero("123456")).thenReturn(Optional.of(cuenta));

        var response = cuentaService.crearCuenta(cuenta);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: El número de cuenta ya existe.", response.getBody());
        verify(cuentaRepository, never()).agregarCuenta(any());
    }

    @Test
    void testCrearCuenta_FalloPorSaldoNegativo() {
        Cuenta cuentaInvalida = new Cuenta("123456", -100.0, 1L);

        var response = cuentaService.crearCuenta(cuentaInvalida);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: El saldo inicial no puede ser negativo.", response.getBody());
        verify(cuentaRepository, never()).agregarCuenta(any());
    }

    @Test
    void testCrearCuenta_FalloPorNumeroVacio() {
        Cuenta cuentaInvalida = new Cuenta("", 500.0, 1L);

        var response = cuentaService.crearCuenta(cuentaInvalida);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: El número de cuenta es obligatorio.", response.getBody());
        verify(cuentaRepository, never()).agregarCuenta(any());
    }

    @Test
    void testObtenerTodasLasCuentas() {
        when(cuentaRepository.obtenerCuentas()).thenReturn(List.of(cuenta));

        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();

        assertNotNull(cuentas);
        assertEquals(1, cuentas.size());
        verify(cuentaRepository, times(1)).obtenerCuentas();
    }

    @Test
    void testActualizarSaldo_Exito() {
        when(cuentaRepository.actualizarCuenta("123456", 700.0)).thenReturn(1);

        var response = cuentaService.actualizarSaldo("123456", 700.0);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Saldo actualizado con éxito.", response.getBody());
        verify(cuentaRepository, times(1)).actualizarCuenta("123456", 700.0);
    }

    @Test
    void testActualizarSaldo_FalloPorSaldoNegativo() {
        var response = cuentaService.actualizarSaldo("123456", -50.0);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: No se puede tener saldo negativo.", response.getBody());
        verify(cuentaRepository, never()).actualizarCuenta(anyString(), anyDouble());
    }

    @Test
    void testActualizarSaldo_CuentaNoEncontrada() {
        when(cuentaRepository.actualizarCuenta("999999", 500.0)).thenReturn(0);

        var response = cuentaService.actualizarSaldo("999999", 500.0);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error: No se pudo actualizar el saldo.", response.getBody());
        verify(cuentaRepository, times(1)).actualizarCuenta("999999", 500.0);
    }


    @Test
    void testEliminarCuenta_Exito() {
        when(cuentaRepository.eliminarCuenta("123456")).thenReturn(1);

        boolean resultado = cuentaService.eliminarCuenta("123456");

        assertTrue(resultado);
        verify(cuentaRepository, times(1)).eliminarCuenta("123456");
    }

    @Test
    void testEliminarCuenta_FalloPorCuentaNoExistente() {
        when(cuentaRepository.eliminarCuenta("999999")).thenReturn(0);

        boolean resultado = cuentaService.eliminarCuenta("999999");

        assertFalse(resultado);
        verify(cuentaRepository, times(1)).eliminarCuenta("999999");
    }
}
