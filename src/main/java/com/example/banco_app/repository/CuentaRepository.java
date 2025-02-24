package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CuentaRepository {
    private static final Logger log = LoggerFactory.getLogger(CuentaRepository.class);
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
        String sql = "INSERT INTO cuenta (numero, saldo, id_cliente) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, cuenta.getNumero(), cuenta.getSaldo(), cuenta.getClienteId());
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Error: El número de cuenta ya existe.");
        }
    }

    public int actualizarCuenta(String numeroCuenta, double nuevoSaldo) {
        log.info("Intentando actualizar saldo de la cuenta '{}'", numeroCuenta);

        long inicio = System.currentTimeMillis();

        String sql = "UPDATE cuenta SET saldo = ? WHERE numero = ?";
        int filas = jdbcTemplate.update(sql, nuevoSaldo, numeroCuenta);

        long fin = System.currentTimeMillis();
        log.info("Tiempo de ejecución del UPDATE: {} ms", (fin - inicio));

        if (filas == 0) {
            log.warn("No se encontró la cuenta con número: '{}'", numeroCuenta);
        } else {
            log.info("Saldo actualizado con éxito. Filas afectadas: {}", filas);
        }

        return filas;
    }


    public int eliminarCuenta(String numero) {
        String sql = "DELETE FROM cuenta WHERE numero = ?";
        return jdbcTemplate.update(sql, numero);
    }

    public static class CuentaRowMapper implements RowMapper<Cuenta> { 
        @Override
        public Cuenta mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Cuenta cuenta = new Cuenta();
            cuenta.setNumero(rs.getString("numero"));  // ✅ Número es la clave primaria
            cuenta.setSaldo(rs.getDouble("saldo"));
            cuenta.setClienteId(rs.getLong("id_cliente")); // 🔹 Aquí se asigna correctamente el id_cliente
            return cuenta;
        }
    }


    }
