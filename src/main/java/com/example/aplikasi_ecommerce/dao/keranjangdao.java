package com.example.aplikasi_ecommerce.dao;
import com.example.aplikasi_ecommerce.model.itemkeranjang;
import com.example.aplikasi_ecommerce.util.databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class keranjangdao {

    public boolean tambahkeranjang(int idPengguna, int idProduk, int jumlah) {
        String query = "INSERT INTO keranjang (id_pengguna, index_produk, jumlah) VALUES (?, ?, ?)";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPengguna);
            stmt.setInt(2, idProduk);
            stmt.setInt(3, jumlah);

            int barisTerpengaruh = stmt.executeUpdate();
            return barisTerpengaruh > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan barang ke keranjang!");
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<com.example.aplikasi_ecommerce.model.itemkeranjang> ambilKeranjangUser(int idPengguna) {
        // Cukup gunakan satu variabel List saja
        List<itemkeranjang> listKeranjang = new ArrayList<>();

        // Kueri SQL Join untuk mengambil nama dan harga dari tabel produk
        String query = "SELECT k.id_keranjang, k.index_produk, p.nama_produk, p.harga_produk, k.jumlah " +
                "FROM keranjang k JOIN produk p ON k.index_produk = p.index_produk " +
                "WHERE k.id_pengguna = ?";

        try (java.sql.Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPengguna);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itemkeranjang item = new itemkeranjang(
                            rs.getInt("id_keranjang"),
                            rs.getInt("index_produk"),
                            rs.getString("nama_produk"),
                            rs.getDouble("harga_produk"),
                            rs.getInt("jumlah")
                    );
                    listKeranjang.add(item);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return listKeranjang;
    }

    // Fungsi 2: Menghapus item dari keranjang
    public boolean hapusItem(int idKeranjang) {
        String query = "DELETE FROM keranjang WHERE id_keranjang = ?";
        try (java.sql.Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idKeranjang);
            return stmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
