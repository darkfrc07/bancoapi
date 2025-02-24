package com.example.banco_app;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class TestDBConnectionTest {

    @Test
    void testConexionExitosa() {
    	String url = "jdbc:mysql://localhost:3306/banco_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "juliana09*";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            assertNotNull(conn, "La conexión no debería ser nula.");
            System.out.println("✅ Conexión exitosa a MySQL!");
        } catch (SQLException e) {
            fail("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
