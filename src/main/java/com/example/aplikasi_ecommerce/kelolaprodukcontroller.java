package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.produkdao;
import com.example.aplikasi_ecommerce.model.Produk;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class kelolaprodukcontroller {

    @FXML private TableView<Produk> tableProduk;
    @FXML private TableColumn<Produk, Integer> colId;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Double> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;
    @FXML private TableColumn<Produk, String> colDesk;

    @FXML private TextField txtNama;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;
    @FXML private TextArea txtDesk;
    @FXML private TextField txtKategori;
    @FXML private TextField txtToko;

    private produkdao produkDAO = new produkdao();
    private ObservableList<Produk> obListProduk;
    private Produk produkTerpilih = null;

    @FXML
    public void initialize() {
        // Hubungkan kolom tabel dengan atribut model Produk
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduk"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("hargaProduk"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colDesk.setCellValueFactory(new PropertyValueFactory<>("desk_produk"));

        segarkanTabel();

        // Listener saat baris tabel diklik oleh user
        tableProduk.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                produkTerpilih = newSelection;
                txtNama.setText(produkTerpilih.getNamaProduk());
                txtHarga.setText(String.valueOf(produkTerpilih.getHargaProduk()));
                txtStok.setText(String.valueOf(produkTerpilih.getStok()));
                txtDesk.setText(produkTerpilih.getDesk_produk());
                txtKategori.setText(String.valueOf(produkTerpilih.getIdkategori()));
                txtToko.setText(String.valueOf(produkTerpilih.getIdtoko()));
                txtToko.setDisable(true); // ID Toko tidak boleh diganti saat edit
            }
        });
    }

    private void segarkanTabel() {
        int idUserAktif = usersession.getPenggunaAktif().getIdpengguna();

        int idTokoSaya = produkDAO.cariIdTokoByAdmin(idUserAktif);

        List<Produk> listDariDB;
        if (idTokoSaya != -1) {
            listDariDB = produkDAO.ambilproduk(idTokoSaya);
        } else {
            listDariDB = new ArrayList<>();
        }

        obListProduk = FXCollections.observableArrayList(listDariDB);
        tableProduk.setItems(obListProduk);
    }

    private void bersihkanForm() {
        txtNama.clear();
        txtHarga.clear();
        txtStok.clear();
        txtDesk.clear();
        txtKategori.clear();
        txtToko.clear();
        txtToko.setDisable(false);
        produkTerpilih = null;
    }

    @FXML
    private void aksiTambah() {
        try {
            String nama = txtNama.getText();
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            String desk = txtDesk.getText();
            int idKategori = Integer.parseInt(txtKategori.getText());
            int idToko = Integer.parseInt(txtToko.getText());

            Produk pBaru = new Produk(0, nama, harga, stok, desk, idToko, idKategori, null);
            if (produkDAO.tambahProduk(pBaru)) {
                tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil ditambahkan!");
                segarkanTabel();
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan produk ke database.");
            }
        } catch (NumberFormatException e) {
            tampilkanAlert(Alert.AlertType.WARNING, "Validasi Error", "Harga, Stok, Kategori, dan Toko harus angka!");
        }
    }

    @FXML
    private void aksiUbah() {
        if (produkTerpilih == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk di tabel terlebih dahulu!");
            return;
        }

        try {
            produkTerpilih.setNamaProduk(txtNama.getText());
            produkTerpilih.setHargaProduk(Double.parseDouble(txtHarga.getText()));
            produkTerpilih.setStok(Integer.parseInt(txtStok.getText()));
            produkTerpilih.setDesk_produk(txtDesk.getText());
            produkTerpilih.setIdkategori(Integer.parseInt(txtKategori.getText()));

            if (produkDAO.ubahProduk(produkTerpilih)) {
                tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil diubah!");
                segarkanTabel();
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui data produk.");
            }
        } catch (NumberFormatException e) {
            tampilkanAlert(Alert.AlertType.WARNING, "Validasi Error", "Input harga atau stok tidak valid!");
        }
    }

    @FXML
    private void aksiHapus() {
        if (produkTerpilih == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk di tabel terlebih dahulu!");
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION, "Hapus produk ini?", ButtonType.YES, ButtonType.NO);
        konfirmasi.showAndWait();

        if (konfirmasi.getResult() == ButtonType.YES) {
            if (produkDAO.hapusProduk(produkTerpilih.getIdProduk())) {
                tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Produk berhasil dihapus!");
                segarkanTabel();
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus produk.");
            }
        }
    }

    @FXML
    private void aksiBersihkan() {
        bersihkanForm();
    }

    private void tampilkanAlert(Alert.AlertType tipe, String judul, String isi) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(isi);
        alert.showAndWait();
    }
}