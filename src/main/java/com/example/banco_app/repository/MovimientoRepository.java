package com.example.banco_app.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.example.banco_app.model.Movimiento;

@Repository
public class MovimientoRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/banco_db";
    private static final String USER = "root";
    private static final String PASSWORD = "juliana09*";

    public Long agregarMovimiento(String numeroCuenta, Movimiento movimiento) {
        String queryInsert = "INSERT INTO movimientos (numero_cuenta, tipo, fecha, valor) VALUES (?, ?, ?, ?)";
        String queryUpdateSaldo = "UPDATE cuentas SET saldo = saldo + ? WHERE numero = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // Iniciar transacción

            Long idGenerado = null;
            try (PreparedStatement stmt = connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, numeroCuenta);
                stmt.setString(2, movimiento.getTipo());
                stmt.setDate(3, Date.valueOf(movimiento.getFecha()));
                stmt.setDouble(4, movimiento.getValor());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idGenerado = generatedKeys.getLong(1);
                    }
                }
            }

            // Ajuste del saldo en la cuenta
            try (PreparedStatement stmt = connection.prepareStatement(queryUpdateSaldo)) {
                double ajusteSaldo = movimiento.getTipo().equalsIgnoreCase("débito") ? -movimiento.getValor() : movimiento.getValor();
                stmt.setDouble(1, ajusteSaldo);
                stmt.setString(2, numeroCuenta);
                stmt.executeUpdate();
            }

            connection.commit();
            return idGenerado;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Movimiento> obtenerMovimientos() {
        List<Movimiento> movimientos = new ArrayList<>();
        String query = "SELECT id, tipo, fecha, valor FROM movimientos";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movimiento movimiento = new Movimiento(
                        rs.getLong("id"),
                        rs.getString("tipo"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("valor")
                );
                movimientos.add(movimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimientos;
    }
}
