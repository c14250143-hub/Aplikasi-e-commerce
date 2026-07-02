package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.toko;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class tokodao {
    public boolean tambahtoko(toko toko) {
        String query = "INSERT INTO toko (id_toko, desk_toko, id_pengguna) VALUES (?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, toko.getIdToko());
            stmt.setString(2, toko.getDeskToko());
            stmt.setInt(3, toko.getIdPengguna());

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan data toko!");
            e.printStackTrace();
            return false;
        }
    }
}
