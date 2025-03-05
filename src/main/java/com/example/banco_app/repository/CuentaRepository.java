package com.example.banco_app.repository;

import com.example.banco_app.model.Cuenta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
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
        String sql = "SELECT numero, saldo, id_cliente FROM cuenta";
        try {
            List<Cuenta> cuentas = jdbcTemplate.query(sql, new CuentaRowMapper());
            log.info("Cuentas recuperadas: {}", cuentas.size());
            return cuentas;
        } catch (DataAccessException e) {
            log.error("Error al obtener cuentas de la base de datos", e);
            throw new RuntimeException("No se pudo obtener la lista de cuentas", e);
        }
    }

    public Optional<Cuenta> buscarPorNumero(String numero) {
        String sql = "SELECT numero, saldo, id_cliente FROM cuenta WHERE numero = ?";
        try {
            List<Cuenta> cuentas = jdbcTemplate.query(sql, new CuentaRowMapper(), numero);
            return cuentas.stream().findFirst();
        } catch (DataAccessException e) {
            log.error("Error al buscar cuenta con número: {}", numero, e);
            return Optional.empty();
        }
    }

    public void agregarCuenta(Cuenta cuenta) {
        String sql = "INSERT INTO cuenta (numero, saldo, id_cliente) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, cuenta.getNumero(), cuenta.getSaldo(), cuenta.getIdCliente());
            log.info("Cuenta agregada con éxito: {}", cuenta.getNumero());
        } catch (DuplicateKeyException e) {
            log.warn("Intento de duplicación: la cuenta {} ya existe", cuenta.getNumero());
            throw new RuntimeException("Error: El número de cuenta ya existe.", e);
        } catch (DataAccessException e) {
            log.error("Error al agregar cuenta: {}", cuenta.getNumero(), e);
            throw new RuntimeException("No se pudo agregar la cuenta", e);
        }
    }

    public int actualizarCuenta(String numeroCuenta, double nuevoSaldo) {
        if (nuevoSaldo < 0) {
            log.warn("Intento de asignar saldo negativo a la cuenta: {}", numeroCuenta);
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }

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
        try {
            int filas = jdbcTemplate.update(sql, numero);
            if (filas > 0) {
                log.info("Cuenta eliminada con éxito: {}", numero);
            } else {
  
                log.warn("No se encontró cuenta con número: {}", numero);
            }
            return filas;
        } catch (DataAccessException e) {
            log.error("Error al eliminar la cuenta: {}", numero, e);
            throw new RuntimeException("No se pudo eliminar la cuenta", e);
        }
    }

    public static class CuentaRowMapper implements RowMapper<Cuenta> {
        @Override
        public Cuenta mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Cuenta cuenta = new Cuenta();
            cuenta.setNumero(rs.getString("numero"));
            cuenta.setSaldo(rs.getDouble("saldo"));
            cuenta.setIdCliente(rs.getLong("id_cliente"));
            return cuenta;
        }
    }
}
