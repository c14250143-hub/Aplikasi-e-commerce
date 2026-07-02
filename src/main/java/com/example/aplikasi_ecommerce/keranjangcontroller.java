package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.keranjangdao;
import com.example.aplikasi_ecommerce.dao.orderdao;
import com.example.aplikasi_ecommerce.model.itemkeranjang;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class keranjangcontroller {

    @FXML private TableView<itemkeranjang> tabelKeranjang;
    @FXML private TableColumn<itemkeranjang, String> colNama;
    @FXML private TableColumn<itemkeranjang, Double> colHarga;
    @FXML private TableColumn<itemkeranjang, Integer> colJumlah;
    @FXML private TableColumn<itemkeranjang, Double> colSubtotal;
    @FXML private Label labelTotal;

    private keranjangdao keranjangDAO = new keranjangdao();
    private ObservableList<itemkeranjang> obListKeranjang;

    @FXML
    public void initialize() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("NamaProduk"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("HargaProduk"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("Jumlah"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("Subtotal"));

        muatDataKeranjang();
    }

    private void muatDataKeranjang() {
        if (usersession.getPenggunaAktif() != null) {
            int idUser = usersession.getPenggunaAktif().getIdpengguna();

            // Mengambil daftar item belanja dari database PostgreSQL melalui DAO
            List<itemkeranjang> listKeranjang = keranjangDAO.ambilKeranjangUser(idUser);

            // Konversi List biasa ke ObservableList JavaFX agar tabel auto-update
            obListKeranjang = FXCollections.observableArrayList(listKeranjang);
            tabelKeranjang.setItems(obListKeranjang);

            // Hitung total belanjaan dan tampilkan pada label
            updateTotalPembayaran();
        } else {
            System.out.println("Warning: Tidak ada pengguna yang login. Gagal memuat keranjang.");
            labelTotal.setText("Rp 0");
        }
    }

    private void updateTotalPembayaran() {
        double total = 0;
        if (obListKeranjang != null) {
            for (itemkeranjang item : obListKeranjang) {
                total += item.getSubtotal();
            }
        }
        // Format tampilan mata uang rupiah pada label total belanja
        labelTotal.setText(String.format("Rp %,.2f", total));
    }

    @FXML
    private void handleHapusBarang(ActionEvent event) {
        // Ambil baris produk yang dipilih oleh user di tabel
        itemkeranjang itemTerpilih = tabelKeranjang.getSelectionModel().getSelectedItem();

        if (itemTerpilih == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih barang yang ingin dihapus terlebih dahulu!");
            alert.showAndWait();
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION, "Hapus " + itemTerpilih.getNamaProduk() + " dari keranjang?", ButtonType.YES, ButtonType.NO);
        konfirmasi.showAndWait();

        if (konfirmasi.getResult() == ButtonType.YES) {
            // Hapus dari database menggunakan DAO
            boolean sukses = keranjangDAO.hapusItem(itemTerpilih.getIdKeranjang());
            if (sukses) {
                // Refresh data tabel setelah penghapusan berhasil
                muatDataKeranjang();
                Alert alertSukses = new Alert(Alert.AlertType.INFORMATION, "Barang berhasil dihapus!");
                alertSukses.showAndWait();
            } else {
                Alert alertGagal = new Alert(Alert.AlertType.ERROR, "Gagal menghapus barang dari database.");
                alertGagal.showAndWait();
            }
        }
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (usersession.getPenggunaAktif() == null) return;

        if (obListKeranjang == null || obListKeranjang.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Keranjang Anda masih kosong!");
            alert.showAndWait();
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin melakukan checkout sekarang?", ButtonType.YES, ButtonType.NO);
        konfirmasi.setTitle("Konfirmasi Checkout");
        konfirmasi.setHeaderText(null);
        konfirmasi.showAndWait();

        if (konfirmasi.getResult() == ButtonType.YES) {
            int idUser = usersession.getPenggunaAktif().getIdpengguna();

            double total = 0;
            for (itemkeranjang item : obListKeranjang) {
                total += item.getSubtotal();
            }

            orderdao orderDAO = new orderdao();
            boolean sukses = orderDAO.prosesCheckout(idUser, total, obListKeranjang);

            if (sukses) {
                Alert alertSukses = new Alert(Alert.AlertType.INFORMATION, "Checkout Berhasil! Pesanan Anda sedang diproses.");
                alertSukses.showAndWait();

                // Bersihkan tampilan tabel keranjang
                muatDataKeranjang();
            } else {
                Alert alertGagal = new Alert(Alert.AlertType.ERROR, "Checkout Gagal! Terjadi kesalahan sistem atau stok habis.");
                alertGagal.showAndWait();
            }
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("uiinterface.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.showAndWait();
        }
    }
}