package com.example.banco_app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
        String sql = "INSERT INTO clientes (nombre, direccion, telefono) VALUES (?, ?, ?)";
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
        }
        return cliente;
    }

    public List<Cliente> obtenerClientes() {
        String sql = "SELECT id, nombre, direccion, telefono FROM clientes";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Cliente.class));
    }

    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, direccion, telefono FROM clientes WHERE id = ?";
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Cliente.class), id);
            return Optional.ofNullable(cliente);
        } catch (DataAccessException e) {
            log.warn("Cliente no encontrado con ID: {}", id);
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        String sql = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ? WHERE id = ?";
        int filasActualizadas = jdbcTemplate.update(sql,
                clienteActualizado.getNombre(),
                clienteActualizado.getDireccion(),
                clienteActualizado.getTelefono(),
                id);
        return filasActualizadas > 0 ? buscarPorId(id) : Optional.empty();
    }

    @Transactional
    public boolean eliminarClientePorId(Long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id) > 0;
        } catch (DataAccessException e) {
            log.error("Error al eliminar cliente con ID: {}", id, e);
            return false;
        }
    }

    @Transactional
    public boolean asignarCuenta(Long clienteId, Long cuentaId) {
        String sql = "UPDATE cuenta SET cliente_id = ? WHERE id = ?";
        if (buscarPorId(clienteId).isEmpty() || buscarCuentaPorId(cuentaId).isEmpty()) {
            log.warn("Asignación fallida: Cliente ID {} o Cuenta ID {} no existen", clienteId, cuentaId);
            return false;
        }
        return jdbcTemplate.update(sql, clienteId, cuentaId) > 0;
    }

    private Optional<Long> buscarCuentaPorId(Long cuentaId) {
        String sql = "SELECT id FROM cuenta WHERE id = ?";
        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, cuentaId);
            return Optional.ofNullable(id);
        } catch (DataAccessException e) {
            log.warn("Cuenta no encontrada con ID: {}", cuentaId);
            return Optional.empty();
        }
    }
}
