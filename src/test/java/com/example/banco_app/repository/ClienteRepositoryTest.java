package com.example.banco_app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
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
        KeyHolder keyHolder = new GeneratedKeyHolder(); // Usar KeyHolder real

        doAnswer(invocation -> {
            KeyHolder keyHolderArg = invocation.getArgument(1);
            keyHolderArg.getKeyList().add(Map.of("id", 1L)); // Simular la generación de clave
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        Cliente resultado = clienteRepository.agregarCliente(new Cliente(null, "Juan Pérez", "Calle 123", "123456789"));

        assertNotNull(resultado.getId(), "El ID del cliente no debería ser nulo");
        assertEquals(1L, resultado.getId());
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
    
    @Test
    void asignarCuenta_CuandoExitoso_DebeRetornarTrue() {
        when(jdbcTemplate.update(anyString(), anyLong(), anyLong())).thenReturn(1);

        boolean resultado = clienteRepository.asignarCuenta(1L, 100L);

        assertTrue(resultado);
        verify(jdbcTemplate).update(anyString(), eq(1L), eq(100L));
    }

    @Test
    void asignarCuenta_CuandoFalla_DebeRetornarFalse() {
        when(jdbcTemplate.update(anyString(), anyLong(), anyLong())).thenReturn(0);

        boolean resultado = clienteRepository.asignarCuenta(1L, 100L);

        assertFalse(resultado);
    }

    @Test
    void asignarCuenta_CuandoExcepcion_DebeLanzarRuntimeException() {
        when(jdbcTemplate.update(anyString(), anyLong(), anyLong()))
                .thenThrow(new DataAccessException("Error de base de datos") {});

        assertThrows(RuntimeException.class, () -> clienteRepository.asignarCuenta(1L, 100L));
    }

    @Test
    void agregarCliente_CuandoExcepcion_DebeLanzarDataAccessException() {
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenThrow(new DataAccessException("Error de base de datos") {});

        assertThrows(DataAccessException.class, () -> clienteRepository.agregarCliente(cliente));
    }

}
