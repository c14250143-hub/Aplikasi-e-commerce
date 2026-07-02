package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.rating;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ratingdao {

    public boolean tambahRating(rating rating) {
        String query = "INSERT INTO rating (nilai_bintang, id_pengguna, id_produk) VALUES (?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rating.getNilaiBintang());
            stmt.setInt(2, rating.getIdPengguna());
            stmt.setInt(3, rating.getIdProduk());

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan Rating!");
            e.printStackTrace();
            return false;
        }
    }
}
