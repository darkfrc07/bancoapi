package com.example.banco_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/banco_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "juliana09*";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conexión exitosa a MySQL!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}