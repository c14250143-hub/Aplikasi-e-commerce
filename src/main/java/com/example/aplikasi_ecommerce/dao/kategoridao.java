package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.kategori;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class kategoridao {

    public List<kategori> getAllkategori() {
        List<kategori> listkategori = new ArrayList<>();
        String query = "SELECT id_kategori, nama_kategori FROM kategori";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                kategori kategori = new kategori(
                        rs.getInt("id_kategori"),
                        rs.getString("nama_kategori")
                );
                listkategori.add(kategori);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data kategori!");
            e.printStackTrace();
        }
        return listkategori;
    }
}