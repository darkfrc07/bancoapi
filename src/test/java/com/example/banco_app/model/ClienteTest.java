package com.example.banco_app.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

class ClienteTest {
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testClienteCreacion() {
        Cliente cliente = new Cliente(1L, "Juan Pérez", "Calle 123", "123456789");

        assertEquals(1L, cliente.getId());
        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("Calle 123", cliente.getDireccion());
        assertEquals("123456789", cliente.getTelefono());
    }

    @Test
    void testClienteSetters() {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNombre("Ana López");
        cliente.setDireccion("Avenida 456");
        cliente.setTelefono("987654321");

        assertEquals(2L, cliente.getId());
        assertEquals("Ana López", cliente.getNombre());
        assertEquals("Avenida 456", cliente.getDireccion());
        assertEquals("987654321", cliente.getTelefono());
    }

    @Test
    void testClienteValidacion() {
        Cliente cliente = new Cliente(null, "", "", "");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Verificamos que hay exactamente 3 violaciones
        assertEquals(3, violations.size());

        // Extraemos los mensajes de error
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        // Comprobamos que los mensajes sean los correctos
        assertTrue(messages.contains("El nombre es obligatorio"));
        assertTrue(messages.contains("La dirección es obligatoria"));
        assertTrue(messages.contains("El teléfono es obligatorio"));
    }
}
