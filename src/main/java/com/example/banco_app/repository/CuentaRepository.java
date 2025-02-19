package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CuentaRepository {
    private final JdbcTemplate jdbcTemplate;

    public CuentaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cuenta> obtenerCuentas() {
        String sql = "SELECT * FROM cuenta";
        return jdbcTemplate.query(sql, new CuentaRowMapper());
    }

    public Optional<Cuenta> buscarPorNumero(String numero) {
        String sql = "SELECT * FROM cuenta WHERE numero = ?";
        List<Cuenta> cuentas = jdbcTemplate.query(sql, new CuentaRowMapper(), numero);
        return cuentas.stream().findFirst();
    }

    public void agregarCuenta(Cuenta cuenta) {
        String sql = "INSERT INTO cuenta (numero, saldo, cliente_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, cuenta.getNumero(), cuenta.getSaldo(), cuenta.getClienteId());
    }

    public int actualizarCuenta(String numero, double saldo) {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numero = ?";
        return jdbcTemplate.update(sql, saldo, numero);
    }

    public int eliminarCuenta(String numero) {
        String sql = "DELETE FROM cuenta WHERE numero = ?";
        return jdbcTemplate.update(sql, numero);
    }

    private static class CuentaRowMapper implements RowMapper<Cuenta> {
        @Override
        public Cuenta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Cuenta(
                rs.getString("numero"),
                rs.getDouble("saldo"),
                rs.getLong("cliente_id")
            );
        }
    }
}