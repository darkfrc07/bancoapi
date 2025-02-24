package com.example.banco_app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.banco_app.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {
    private static final Logger log = LoggerFactory.getLogger(ClienteRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Cliente agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, direccion, telefono) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getDireccion());
                ps.setString(3, cliente.getTelefono());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                cliente.setId(keyHolder.getKey().longValue());
            }
        } catch (DataAccessException e) {
            log.error("Error al agregar cliente: {}", cliente, e);
            throw e;
        }

        return cliente;
    }

    public List<Cliente> obtenerClientes() {
        String sql = "SELECT id, nombre, direccion, telefono FROM cliente";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Cliente.class));
    }

    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, direccion, telefono FROM cliente WHERE id = ?";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Cliente.class), id);
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Cliente no encontrado con ID: {}", id);
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Error al buscar cliente con ID: {}", id, e);
            throw new RuntimeException("Error en la base de datos", e);
        }
    }

    @Transactional
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        String sql = "UPDATE cliente SET nombre = ?, direccion = ?, telefono = ? WHERE id = ?";
        
        int filasAfectadas = jdbcTemplate.update(sql,
                clienteActualizado.getNombre(),
                clienteActualizado.getDireccion(),
                clienteActualizado.getTelefono(),
                id);

        if (filasAfectadas > 0) {
            clienteActualizado.setId(id);
            return Optional.of(clienteActualizado);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean eliminarClientePorId(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
    
    @Transactional
    public boolean asignarCuenta(Long clienteId, Long cuentaId) {
        String sql = "UPDATE cuenta SET cliente_id = ? WHERE id = ?";
        
        try {
            int filasAfectadas = jdbcTemplate.update(sql, clienteId, cuentaId);
            return filasAfectadas > 0;
        } catch (DataAccessException e) {
            log.error("Error al asignar la cuenta {} al cliente {}", cuentaId, clienteId, e);
            throw new RuntimeException("Error en la base de datos al asignar cuenta", e);
        }
    }

}
