package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.orderdao;
import com.example.aplikasi_ecommerce.dao.produkdao;
import com.example.aplikasi_ecommerce.model.order;
import com.example.aplikasi_ecommerce.model.pengguna;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class ordercontroller {
    @FXML private TableView<order> tabelPesanan;
    @FXML private TableColumn<order, Integer> colIdOrder;
    @FXML private TableColumn<order, String> colTanggal;
    @FXML private TableColumn<order, Double> colTotal;
    @FXML private TableColumn<order, String> colStatus;

    @FXML private ComboBox<String> comboStatus;

    private orderdao orderDAO = new orderdao();
    private produkdao produkDAO = new produkdao();
    private int idTokoMilikAdmin = -1;

    @FXML
    public void initialize() {
        colIdOrder.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusPesanan"));

        comboStatus.getItems().addAll(
                "Menunggu Pembayaran",
                "Sudah Dibayar",
                "Sedang Diproses",
                "Dikirim",
                "Selesai",
                "Dibatalkan"
        );

        pengguna adminAktif = usersession.getPenggunaAktif();
        if (adminAktif != null && "Admin".equalsIgnoreCase(adminAktif.getRole())) {
            idTokoMilikAdmin = produkDAO.cariIdTokoByAdmin(adminAktif.getIdpengguna());
            if (idTokoMilikAdmin == -1) idTokoMilikAdmin = adminAktif.getIdpengguna();
        }

        muatDataPesanan();
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        muatDataPesanan();
    }

    private void muatDataPesanan() {
        if (idTokoMilikAdmin > 0) {
            // Memanggil DAO untuk mengambil daftar orderan KHUSUS untuk toko ini
            List<order> daftarPesanan = orderDAO.ambilPesananMasukToko(idTokoMilikAdmin);
            ObservableList<order> data = FXCollections.observableArrayList(daftarPesanan);
            tabelPesanan.setItems(data);
        } else {
            System.out.println("Warning: Admin tidak memiliki ID Toko yang valid.");
        }
    }

    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        order pesananTerpilih = tabelPesanan.getSelectionModel().getSelectedItem();
        if (pesananTerpilih == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan klik/pilih salah satu pesanan di tabel terlebih dahulu.");
            return;
        }

        String statusBaru = comboStatus.getValue();
        if (statusBaru == null || statusBaru.isEmpty()) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih status baru dari kotak pilihan (ComboBox).");
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                "Ubah status Pesanan #" + pesananTerpilih.getIdOrder() + " menjadi '" + statusBaru + "'?",
                ButtonType.YES, ButtonType.NO);
        konfirmasi.setTitle("Konfirmasi Pembaruan Status");
        konfirmasi.setHeaderText(null);
        konfirmasi.showAndWait();

        if (konfirmasi.getResult() == ButtonType.YES) {
            boolean sukses = orderDAO.updateStatusPesanan(pesananTerpilih.getIdOrder(), statusBaru);
            if (sukses) {
                tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Status pesanan berhasil diperbarui!");
                muatDataPesanan();
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menyimpan status ke database.");
            }
        }
    }

    private void tampilkanAlert(Alert.AlertType tipe, String judul, String isi) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(isi);
        alert.showAndWait();
    }
}
