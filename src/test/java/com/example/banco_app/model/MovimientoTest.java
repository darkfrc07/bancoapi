package com.example.banco_app.model;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

class MovimientoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testMovimientoCreacion() {
        Movimiento movimiento = new Movimiento(1L, "DEBITO", LocalDate.of(2024, 2, 21), 500.0, "123456789");

        assertEquals(Long.valueOf(1), movimiento.getId()); 
        assertEquals("DEBITO", movimiento.getTipo());
        assertEquals(LocalDate.of(2024, 2, 21), movimiento.getFecha());
        assertEquals(500.0, movimiento.getValor());
        assertEquals("123456789", movimiento.getNumeroCuenta());
    }

    @Test
    void testMovimientoSetters() {
        Movimiento movimiento = new Movimiento(null, "CREDITO", null, 0, null);
        movimiento.setId(2L);
        movimiento.setTipo("CREDITO");
        movimiento.setFecha(LocalDate.of(2024, 3, 10));
        movimiento.setValor(1500.0);
        movimiento.setNumeroCuenta("987654321");

        assertEquals(Long.valueOf(2), movimiento.getId());
        assertEquals("CREDITO", movimiento.getTipo());
        assertEquals(LocalDate.of(2024, 3, 10), movimiento.getFecha());
        assertEquals(1500.0, movimiento.getValor());
        assertEquals("987654321", movimiento.getNumeroCuenta());
    }

    @Test
    void testValidacionMovimientoInvalido() {
        Movimiento movimiento = new Movimiento("", -500.0, ""); 
        Set<ConstraintViolation<Movimiento>> violations = validator.validate(movimiento);

        assertEquals(3, violations.size()); 
    }


    @Test
    void testValorNegativoLanzaExcepcion() {
        Movimiento movimiento = new Movimiento(null, "DEBITO", null, 0, null); 
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimiento.setValor(-200);
        });

        assertEquals("El valor del movimiento no puede ser negativo", exception.getMessage());
    }
  }
