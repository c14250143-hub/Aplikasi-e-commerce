package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.ratingprodukreport;
import com.example.aplikasi_ecommerce.model.ratingtokoreport;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class reportdao {

    public List<ratingprodukreport> ambilReportProduk(int filterIdToko) {
        List<ratingprodukreport> list = new ArrayList<>();

        String query = "SELECT p.id_toko, p.index_produk, p.nama_produk, " +
                "COUNT(r.id_rating) AS total_penilai, " +
                "ROUND(AVG(r.nilai_bintang), 2) AS rata_rata_rating " +
                "FROM produk p JOIN rating r ON p.index_produk = r.id_produk ";

        if (filterIdToko > 0) {
            query += "WHERE p.id_toko = ? ";
        }

        query += "GROUP BY p.id_toko, p.index_produk, p.nama_produk " +
                "ORDER BY rata_rata_rating DESC, total_penilai DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (filterIdToko > 0) {
                stmt.setInt(1, filterIdToko);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ratingprodukreport(
                            rs.getInt("id_toko"),
                            rs.getInt("index_produk"),
                            rs.getString("nama_produk"),
                            rs.getInt("total_penilai"),
                            rs.getDouble("rata_rata_rating")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ratingtokoreport> ambilReportToko(int filterIdToko) {
        List<ratingtokoreport> list = new ArrayList<>();
        String query = "SELECT p.id_toko, " +
                "ROUND(AVG(r.nilai_bintang), 2) AS rating_keseluruhan_toko, " +
                "COUNT(r.id_rating) AS total_feedback_masuk, " +
                "CASE " +
                "    WHEN AVG(r.nilai_bintang) >= 4.5 THEN 'Sangat Puas (Excellent)' " +
                "    WHEN AVG(r.nilai_bintang) >= 3.5 THEN 'Puas (Good)' " +
                "    ELSE 'Perlu Evaluasi (Bad)' " +
                "END AS status_reputasi " +
                "FROM rating r JOIN produk p ON r.id_produk = p.index_produk ";

        if (filterIdToko > 0) {
            query += "WHERE p.id_toko = ? ";
        }

        query += "GROUP BY p.id_toko " +
                "ORDER BY rating_keseluruhan_toko DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (filterIdToko > 0) {
                stmt.setInt(1, filterIdToko);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ratingtokoreport(
                            rs.getInt("id_toko"),
                            rs.getDouble("rating_keseluruhan_toko"),
                            rs.getInt("total_feedback_masuk"),
                            rs.getString("status_reputasi")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void cetakLaporanTransaksiUser(int idPengguna, String tanggalAwal, String tanggalAkhir) {
        String query = "SELECT o.id_order, o.date_created AS tanggal_transaksi, p.nama_produk, " +
                "od.qty AS jumlah_beli, od.sub_total, o.status_pesanan " +
                "FROM orders o " +
                "JOIN order_detail od ON o.id_order = od.id_order " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "WHERE o.id_pengguna = ? AND o.date_created BETWEEN ?::timestamp AND ?::timestamp " +
                "ORDER BY o.date_created DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPengguna);
            stmt.setString(2, tanggalAwal + " 00:00:00");
            stmt.setString(3, tanggalAkhir + " 23:59:59");

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("=== LAPORAN TRANSAKSI USER #" + idPengguna + " ===");
                while (rs.next()) {
                    System.out.println("Order ID: " + rs.getInt("id_order") +
                            " | Tanggal: " + rs.getString("tanggal_transaksi") +
                            " | Produk: " + rs.getString("nama_produk") +
                            " | Qty: " + rs.getInt("jumlah_beli") +
                            " | Subtotal: Rp" + rs.getDouble("sub_total") +
                            " | Status: " + rs.getString("status_pesanan"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal menarik Laporan Transaksi User!");
            e.printStackTrace();
        }
    }


    public void cetakLaporanPendapatanToko() {
        String query = "SELECT p.id_toko, " +
                "COUNT(DISTINCT o.id_order) AS jumlah_transaksi_toko, " +
                "SUM(od.sub_total) AS total_pendapatan_toko " +
                "FROM order_detail od " +
                "JOIN orders o ON od.id_order = o.id_order " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "WHERE o.status_pesanan = 'Selesai' " +
                "GROUP BY p.id_toko " +
                "ORDER BY total_pendapatan_toko DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("=== LAPORAN PENDAPATAN TOKO (TRANSAKSI SELESAI) ===");
            while (rs.next()) {
                System.out.println("ID Toko: " + rs.getInt("id_toko") +
                        " | Total Transaksi: " + rs.getInt("jumlah_transaksi_toko") +
                        " | Total Pendapatan: Rp" + String.format("%,.2f", rs.getDouble("total_pendapatan_toko")));
            }
        } catch (SQLException e) {
            System.err.println("Gagal menarik Laporan Pendapatan Toko!");
            e.printStackTrace();
        }
    }


    public void cetakLaporanKategoriTerlaris() {
        String query = "SELECT k.id_kategori, k.nama_kategori, " +
                "SUM(od.qty) AS total_produk_terjual, " +
                "ROUND(AVG(od.sub_total), 2) AS rata_rata_nilai_nota " +
                "FROM order_detail od " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "JOIN kategori k ON p.id_kategori = k.id_kategori " +
                "GROUP BY k.id_kategori, k.nama_kategori " +
                "HAVING SUM(od.qty) >= 5 " +
                "ORDER BY total_produk_terjual DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("=== LAPORAN KATEGORI TERLARIS (MIN. 5 TERJUAL) ===");
            while (rs.next()) {
                System.out.println("Kategori: " + rs.getString("nama_kategori") + " (ID: " + rs.getInt("id_kategori") + ")" +
                        " | Total Terjual: " + rs.getInt("total_produk_terjual") + " unit" +
                        " | Rata-rata Nilai: Rp" + String.format("%,.2f", rs.getDouble("rata_rata_nilai_nota")));
            }
        } catch (SQLException e) {
            System.err.println("Gagal menarik Laporan Kategori Terlaris!");
            e.printStackTrace();
        }
    }

}