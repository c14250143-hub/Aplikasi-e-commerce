package com.example.aplikasi_ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/aplikasi_online_shop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "cain";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL tidak ditemukan! Periksa pom.xml Anda.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
