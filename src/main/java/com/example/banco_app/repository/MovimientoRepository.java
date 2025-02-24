package com.example.banco_app.repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Movimiento;

@Repository
public class MovimientoRepository {
    private final JdbcTemplate jdbcTemplate;

    public MovimientoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Movimiento> obtenerMovimientos() {
        String sql = "SELECT id, numero_cuenta, tipo, fecha, valor FROM movimientos";
        return jdbcTemplate.query(sql, new MovimientoMapper());
    }

    public Long agregarMovimiento(String numeroCuenta, Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (numero_cuenta, tipo, fecha, valor) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((PreparedStatementCreator) connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, numeroCuenta);
            stmt.setString(2, movimiento.getTipo());
            stmt.setDate(3, Date.valueOf(movimiento.getFecha()));
            stmt.setDouble(4, movimiento.getValor());
            return stmt;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    public List<Movimiento> buscarPorNumeroCuenta(String numeroCuenta) {
        String sql = "SELECT id, numero_cuenta, tipo, fecha, valor FROM movimientos WHERE numero_cuenta = ?";
        return jdbcTemplate.query(sql, new MovimientoMapper(), numeroCuenta);
    }

    public Optional<Movimiento> buscarPorId(Long id) {
        String sql = "SELECT id, numero_cuenta, tipo, fecha, valor FROM movimientos WHERE id = ?";
        List<Movimiento> movimientos = jdbcTemplate.query(sql, new MovimientoMapper(), id);
        return movimientos.isEmpty() ? Optional.empty() : Optional.of(movimientos.get(0));
    }

    public int actualizarMovimiento(Long id, Movimiento movimientoActualizado) {
        String sql = "UPDATE movimientos SET tipo = ?, fecha = ?, valor = ? WHERE id = ?";
        return jdbcTemplate.update(sql, movimientoActualizado.getTipo(), Date.valueOf(movimientoActualizado.getFecha()), movimientoActualizado.getValor(),id);
    }

    private static class MovimientoMapper implements RowMapper<Movimiento> {
        @Override
        public Movimiento mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Movimiento(
                rs.getLong("id"),
                rs.getString("tipo"),
                rs.getDate("fecha").toLocalDate(),
                rs.getDouble("valor"),
                rs.getString("numero_cuenta")
            );
        }
    }
}
