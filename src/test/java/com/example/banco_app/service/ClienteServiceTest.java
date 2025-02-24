package com.example.banco_app.service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Juan Pérez", "Calle 123", "123456789");
    }

    @Test
    void testObtenerClientes() {
        when(clienteRepository.obtenerClientes()).thenReturn(List.of(cliente));

        List<Cliente> clientes = clienteService.obtenerClientes();

        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals(cliente, clientes.get(0));
        verify(clienteRepository, times(1)).obtenerClientes();
    }

    @Test
    void testBuscarPorId_ClienteExiste() {
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(cliente, resultado.get());
        verify(clienteRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarPorId_ClienteNoExiste() {
        when(clienteRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).buscarPorId(99L);
    }

    @Test
    void testAgregarCliente() {
        when(clienteRepository.agregarCliente(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.agregarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(clienteRepository, times(1)).agregarCliente(cliente);
    }

    @Test
    void testAgregarClienteConDatosInvalidos() {
        Cliente clienteInvalido = new Cliente(2L, "", "", "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.agregarCliente(clienteInvalido);
        });

        assertEquals("Los campos nombre, dirección y teléfono son obligatorios", exception.getMessage());
        verify(clienteRepository, never()).agregarCliente(any());
    }

    @Test
    void testActualizarCliente() {
        Cliente clienteActualizado = new Cliente(1L, "Juan Pérez Actualizado", "Nueva Calle 456", "987654321");

        when(clienteRepository.actualizarCliente(eq(1L), any(Cliente.class))).thenReturn(Optional.of(clienteActualizado));

        Optional<Cliente> resultado = clienteService.actualizarCliente(1L, clienteActualizado);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez Actualizado", resultado.get().getNombre());
        verify(clienteRepository, times(1)).actualizarCliente(1L, clienteActualizado);
    }

    @Test
    void testEliminarClientePorId() {
        when(clienteRepository.eliminarClientePorId(1L)).thenReturn(true);

        boolean resultado = clienteService.eliminarClientePorId(1L);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).eliminarClientePorId(1L);
    }

    @Test
    void testEliminarClienteInexistente() {
        when(clienteRepository.eliminarClientePorId(99L)).thenReturn(false);

        boolean resultado = clienteService.eliminarClientePorId(99L);

        assertFalse(resultado);
        verify(clienteRepository, times(1)).eliminarClientePorId(99L);
    }

    @Test
    void testAsignarCuenta() {
        when(clienteRepository.asignarCuenta(1L, 100L)).thenReturn(true);

        boolean resultado = clienteService.asignarCuenta(1L, 100L);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).asignarCuenta(1L, 100L);
    }
}
