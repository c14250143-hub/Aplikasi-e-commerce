package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.wishlist;
import com.example.aplikasi_ecommerce.util.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class wishlistdao {

    public boolean tambahKeWishlist(wishlist wishlist) {
        String query = "INSERT INTO wishlist (id_pengguna, id_produk) VALUES (?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, wishlist.getIdPengguna());
            stmt.setInt(2, wishlist.getIdProduk());

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan produk ke Wishlist!");
            e.printStackTrace();
            return false;
        }
    }
}