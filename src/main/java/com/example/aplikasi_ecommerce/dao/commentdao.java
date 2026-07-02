package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.comment;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;

public class commentdao {

    public boolean tambahKomentar(comment comment) {
        String query = "INSERT INTO comment (isi_komentar, tanggal_komentar, id_pengguna, id_produk) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, comment.getIsiKomentar());
            stmt.setTimestamp(2, Timestamp.valueOf(comment.getTanggalKomentar()));
            stmt.setInt(3, comment.getIdPengguna());
            stmt.setInt(4, comment.getIdProduk());

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal memposting komentar!");
            e.printStackTrace();
            return false;
        }
    }
}