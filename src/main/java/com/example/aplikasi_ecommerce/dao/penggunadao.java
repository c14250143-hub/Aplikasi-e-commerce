package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.pengguna;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class penggunadao {

    // 1. Fungsi Registrasi (Insert Pengguna Baru)
    public boolean insertpengguna(pengguna pengguna) {
        // Menambahkan kolom role ke dalam query INSERT
        String query = "INSERT INTO pengguna (nama, email, password, alamat, telephone, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pengguna.getNama());
            stmt.setString(2, pengguna.getEmail());
            stmt.setString(3, pengguna.getPassword());
            stmt.setString(4, pengguna.getAlamat());
            stmt.setString(5, pengguna.getTelephone());
            // Jika role tidak ditentukan (null), otomatis diset sebagai "Customer"
            stmt.setString(6, pengguna.getRole() != null ? pengguna.getRole() : "Customer");

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan data pengguna baru!");
            e.printStackTrace();
            return false;
        }
    }

    // 2. Fungsi Autentikasi Login (Membaca kolom role)
    public pengguna autentikasiLogin(String email, String password) {
        String query = "SELECT * FROM pengguna WHERE email = ? AND password = ?";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Memasukkan parameter ke-7 yaitu role dari database
                    return new pengguna(
                            rs.getInt("id_pengguna"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("alamat"),
                            rs.getString("telephone"),
                            rs.getString("role") // <--- TAMBAHAN WAJIB
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("\n=== ADA ERROR DATABASE DI SINI ===");
            System.err.println("Pesan Error Asli: " + e.getMessage());
            System.err.println("State: " + e.getSQLState());
            System.err.println("==================================\n");
        }
        return null;
    }
}