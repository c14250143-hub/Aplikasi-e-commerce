package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.Produk;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class produkdao {
    public List<Produk> ambilsemuaproduk() {
        List<Produk> daftarProduk = new ArrayList<>();
        String query = "SELECT index_produk, nama_produk, harga_produk, stok, desk_produk, id_toko, id_kategori FROM produk";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produk produk = new Produk(
                        rs.getInt("index_produk"),
                        rs.getString("nama_produk"),
                        rs.getDouble("harga_produk"),
                        rs.getInt("stok"),
                        rs.getString("desk_produk"),
                        rs.getInt("id_toko"),
                        rs.getInt("id_kategori"),
                        null
                );
                daftarProduk.add(produk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarProduk;
    }

    public List<Produk> ambilproduk(int idToko) {
        List<Produk> daftarProduk = new ArrayList<>();
        String query = "SELECT index_produk, nama_produk, harga_produk, stok, desk_produk, id_toko, id_kategori " +
                "FROM produk WHERE id_toko = ?";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idToko);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produk produk = new Produk(
                            rs.getInt("index_produk"),
                            rs.getString("nama_produk"),
                            rs.getDouble("harga_produk"),
                            rs.getInt("stok"),
                            rs.getString("desk_produk"),
                            rs.getInt("id_toko"),
                            rs.getInt("id_kategori"),
                            null
                    );
                    daftarProduk.add(produk);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal memilah produk berdasarkan ID Toko!");
            e.printStackTrace();
        }
        return daftarProduk;
    }

    public int cariIdTokoByAdmin(int idPengguna) {
        String query = "SELECT id_toko FROM toko WHERE id_pengguna = ?";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPengguna);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_toko");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean tambahProduk(Produk produk) {
        String query = "INSERT INTO produk (nama_produk, harga_produk, stok, desk_produk, id_toko, id_kategori) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, produk.getNamaProduk());
            stmt.setDouble(2, produk.getHargaProduk());
            stmt.setInt(3, produk.getStok());
            stmt.setString(4, produk.getDesk_produk());
            stmt.setInt(5, produk.getIdtoko());
            stmt.setInt(6, produk.getIdkategori());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean ubahProduk(Produk produk) {
        String query = "UPDATE produk SET nama_produk = ?, harga_produk = ?, stok = ?, desk_produk = ?, id_kategori = ? WHERE index_produk = ?";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, produk.getNamaProduk());
            stmt.setDouble(2, produk.getHargaProduk());
            stmt.setInt(3, produk.getStok());
            stmt.setString(4, produk.getDesk_produk());
            stmt.setInt(5, produk.getIdkategori());
            stmt.setInt(6, produk.getIdProduk()); // idProduk memetakan ke index_produk

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. FUNGSI HAPUS PRODUK (DELETE)
    public boolean hapusProduk(int indexProduk) {
        String query = "DELETE FROM produk WHERE index_produk = ?";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, indexProduk);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}