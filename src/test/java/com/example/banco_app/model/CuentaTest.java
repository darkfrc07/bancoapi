package com.example.banco_app.model;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CuentaTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCuentaCreacion() {
        Cuenta cuenta = new Cuenta("123456789", 1000.0, 1L);

        assertEquals("123456789", cuenta.getNumero());
        assertEquals(1000.0, cuenta.getSaldo());
        assertEquals(Long.valueOf(1), cuenta.getClienteId()); // Cambio de comparación
    }

    @Test
    void testCuentaSetters() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumero("987654321");
        cuenta.setSaldo(5000.0);
        cuenta.setClienteId(2L);

        assertEquals("987654321", cuenta.getNumero());
        assertEquals(5000.0, cuenta.getSaldo());
        assertEquals(Long.valueOf(2), cuenta.getClienteId());
    }

    @Test
    void testValidacionCuentaInvalida() {
        Cuenta cuenta = new Cuenta("", -50.0, null);

        Set<ConstraintViolation<Cuenta>> violations = validator.validate(cuenta);

        assertEquals(3, violations.size());
    }

    @Test
    void testSaldoNegativoLanzaExcepcion() {
        Cuenta cuenta = new Cuenta();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cuenta.setSaldo(-100);
        });

        assertEquals("El saldo no puede ser negativo", exception.getMessage());
    }
}
