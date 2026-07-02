package com.example.aplikasi_ecommerce.dao;

import com.example.aplikasi_ecommerce.model.itemkeranjang;
import com.example.aplikasi_ecommerce.model.order;
import com.example.aplikasi_ecommerce.util.databaseconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class orderdao {


    public boolean prosesCheckout(int idPengguna, double total, List<itemkeranjang> listItems) {
        Connection conn = null;
        PreparedStatement stmtOrder = null;
        PreparedStatement stmtDetail = null;
        PreparedStatement stmtStok = null;
        PreparedStatement stmtClearCart = null;

        // Default status pesanan saat checkout adalah 'Menunggu Pembayaran'
        String queryOrder = "INSERT INTO orders (id_pengguna, total, status_pesanan, date_created) VALUES (?, ?, 'Menunggu Pembayaran', CURRENT_TIMESTAMP)";
        String queryDetail = "INSERT INTO order_detail (id_order, index_produk, qty, sub_total) VALUES (?, ?, ?, ?)";
        String queryStok = "UPDATE produk SET stok = stok - ? WHERE index_produk = ?";
        String queryClearCart = "DELETE FROM keranjang WHERE id_pengguna = ?";

        try {
            conn = databaseconnection.getConnection();
            conn.setAutoCommit(false); // Memulai SQL Transaction

            // 1. Insert ke tabel orders
            stmtOrder = conn.prepareStatement(queryOrder, Statement.RETURN_GENERATED_KEYS);
            stmtOrder.setInt(1, idPengguna);
            stmtOrder.setDouble(2, total);
            stmtOrder.executeUpdate();

            int idOrderBaru = -1;
            try (ResultSet rs = stmtOrder.getGeneratedKeys()) {
                if (rs.next()) {
                    idOrderBaru = rs.getInt(1);
                }
            }

            if (idOrderBaru == -1) {
                throw new SQLException("Gagal mendapatkan Auto-Generated ID untuk Order Baru.");
            }

            // 2. Insert ke detail order & kurangi stok produk
            stmtDetail = conn.prepareStatement(queryDetail);
            stmtStok = conn.prepareStatement(queryStok);

            for (itemkeranjang item : listItems) {
                // Simpan detail order
                stmtDetail.setInt(1, idOrderBaru);
                stmtDetail.setInt(2, item.getIndexProduk());
                stmtDetail.setInt(3, item.getJumlah());
                stmtDetail.setDouble(4, item.getSubtotal());
                stmtDetail.addBatch();

                // Kurangi stok barang
                stmtStok.setInt(1, item.getJumlah());
                stmtStok.setInt(2, item.getIndexProduk());
                stmtStok.addBatch();
            }

            stmtDetail.executeBatch();
            stmtStok.executeBatch();

            // 3. Bersihkan keranjang belanja
            stmtClearCart = conn.prepareStatement(queryClearCart);
            stmtClearCart.setInt(1, idPengguna);
            stmtClearCart.executeUpdate();

            conn.commit(); // Eksekusi seluruh rangkaian transaksi jika sukses semua
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Batalkan semua proses jika ada salah satu yang gagal
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmtOrder != null) stmtOrder.close();
                if (stmtDetail != null) stmtDetail.close();
                if (stmtStok != null) stmtStok.close();
                if (stmtClearCart != null) stmtClearCart.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<order> ambilRiwayatUser(int idPengguna) {
        List<order> listRiwayat = new ArrayList<>();
        // PERBAIKAN: Disesuaikan dengan nama kolom yang benar
        String query = "SELECT id_order, total, status_pesanan, date_created FROM orders WHERE id_pengguna = ? ORDER BY date_created DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPengguna);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // PERBAIKAN: Konstruktor order sudah disamakan dengan file order.java yang baru
                    order pesanan = new order(
                            rs.getInt("id_order"),
                            idPengguna,
                            rs.getDouble("total"),
                            rs.getString("status_pesanan"),
                            rs.getString("date_created") // getString otomatis mengkonversi TIMESTAMP ke teks yang mudah dibaca JavaFX
                    );
                    listRiwayat.add(pesanan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data riwayat pesanan!");
            e.printStackTrace();
        }
        return listRiwayat;
    }




    public List<order> ambilSemuaOrder() {
        List<order> list = new ArrayList<>();
        String query = "SELECT id_order, id_pengguna, total, status_pesanan, date_created FROM orders ORDER BY id_order DESC";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new order(
                        rs.getInt("id_order"),
                        rs.getInt("id_pengguna"),
                        rs.getDouble("total"),
                        rs.getString("status_pesanan"),
                        rs.getString("date_created")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatusPesanan(int idOrder, String statusBaru) {
        String query = "UPDATE orders SET status_pesanan = ? WHERE id_order = ?";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, statusBaru);
            stmt.setInt(2, idOrder);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gagal merubah status pesanan!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusOrder(int idOrder, String statusBaru) {
        String query = "UPDATE orders SET status_pesanan = ? WHERE id_order = ?";
        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, statusBaru);
            stmt.setInt(2, idOrder);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<order> ambilPesananMasukToko(int idToko) {
        List<order> list = new ArrayList<>();
        // Query menggunakan DISTINCT agar order_id tidak ganda jika ada banyak barang di 1 nota
        String query = "SELECT DISTINCT o.id_order, o.id_pengguna, o.total, o.status_pesanan, o.date_created " +
                "FROM orders o " +
                "JOIN order_detail od ON o.id_order = od.id_order " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "WHERE p.id_toko = ? " +
                "ORDER BY o.date_created DESC";

        try (java.sql.Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idToko);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new order(
                            rs.getInt("id_order"),
                            rs.getInt("id_pengguna"),
                            rs.getDouble("total"),
                            rs.getString("status_pesanan"),
                            rs.getString("date_created")
                    ));
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Gagal mengambil pesanan masuk untuk toko!");
            e.printStackTrace();
        }
        return list;
    }



    public java.util.List<com.example.aplikasi_ecommerce.model.orderdetail> ambilDetailOrder(int idOrder) {
        java.util.List<com.example.aplikasi_ecommerce.model.orderdetail> list = new java.util.ArrayList<>();

        String query = "SELECT od.id_order_detail, od.qty, od.sub_total, od.id_order, od.index_produk, " +
                "p.nama_produk, p.harga_produk " +
                "FROM order_detail od " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "WHERE od.id_order = ?";

        try (Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idOrder);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Memanggil konstruktor baru orderdetail yang menerima 7 parameter
                    list.add(new com.example.aplikasi_ecommerce.model.orderdetail(
                            rs.getInt("id_order_detail"),
                            rs.getInt("qty"),
                            rs.getDouble("sub_total"),
                            rs.getInt("id_order"),
                            rs.getInt("index_produk"),
                            rs.getString("nama_produk"),   // Mengisi field nama produk baru
                            rs.getDouble("harga_produk")   // Mengisi field harga satuan baru
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil detail order untuk nota!");
            e.printStackTrace();
        }
        return list;
    }
}
