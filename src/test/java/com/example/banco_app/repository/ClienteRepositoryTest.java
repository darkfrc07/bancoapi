package com.example.banco_app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;

import com.example.banco_app.model.Cliente;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    
    @InjectMocks
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Juan Pérez", "Calle 123", "123456789");
    }

    @Test
    void agregarCliente_DebeRetornarClienteConId() {
        KeyHolder mockKeyHolder = mock(KeyHolder.class);
        when(mockKeyHolder.getKey()).thenReturn(1L);

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenReturn(1);

        Cliente resultado = clienteRepository.agregarCliente(new Cliente(null, "Juan Pérez", "Calle 123", "123456789"));

        assertNotNull(resultado.getId());
        assertEquals(1L, resultado.getId());

        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void obtenerClientes_DebeRetornarListaClientes() {
        List<Cliente> clientes = List.of(cliente);

        when(jdbcTemplate.query(eq("SELECT id, nombre, direccion, telefono FROM cliente"),
                any(BeanPropertyRowMapper.class)))
                .thenReturn(clientes);

        List<Cliente> resultado = clienteRepository.obtenerClientes();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorId_CuandoClienteExiste_DebeRetornarCliente() {
        when(jdbcTemplate.queryForObject(eq("SELECT id, nombre, direccion, telefono FROM cliente WHERE id = ?"),
                any(BeanPropertyRowMapper.class), anyLong()))
                .thenReturn(cliente);

        Optional<Cliente> resultado = clienteRepository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_CuandoClienteNoExiste_DebeRetornarVacio() {
        when(jdbcTemplate.queryForObject(eq("SELECT id, nombre, direccion, telefono FROM cliente WHERE id = ?"),
                any(BeanPropertyRowMapper.class), anyLong()))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<Cliente> resultado = clienteRepository.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizarCliente_CuandoExitoso_DebeRetornarClienteActualizado() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any())).thenReturn(1);

        Optional<Cliente> resultado = clienteRepository.actualizarCliente(1L, cliente);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
    }

    @Test
    void actualizarCliente_CuandoNoExiste_DebeRetornarVacio() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any())).thenReturn(0);

        Optional<Cliente> resultado = clienteRepository.actualizarCliente(99L, cliente);

        assertFalse(resultado.isPresent());
    }

    @Test
    void eliminarClientePorId_CuandoExitoso_DebeRetornarTrue() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        boolean resultado = clienteRepository.eliminarClientePorId(1L);

        assertTrue(resultado);
    }

    @Test
    void eliminarClientePorId_CuandoFalla_DebeRetornarFalse() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        boolean resultado = clienteRepository.eliminarClientePorId(99L);

        assertFalse(resultado);
    }
}
