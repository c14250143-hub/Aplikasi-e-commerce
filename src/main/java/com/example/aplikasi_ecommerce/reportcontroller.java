package com.example.aplikasi_ecommerce;
import com.example.aplikasi_ecommerce.dao.produkdao;
import com.example.aplikasi_ecommerce.dao.reportdao;
import com.example.aplikasi_ecommerce.model.pengguna;
import com.example.aplikasi_ecommerce.model.ratingprodukreport;
import com.example.aplikasi_ecommerce.model.ratingtokoreport;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class reportcontroller {

    @FXML private TableView<ratingprodukreport> tableProduk;
    @FXML private TableColumn<ratingprodukreport, Integer> colProdToko;
    @FXML private TableColumn<ratingprodukreport, Integer> colProdId;
    @FXML private TableColumn<ratingprodukreport, String> colProdNama;
    @FXML private TableColumn<ratingprodukreport, Integer> colProdTotalReview;
    @FXML private TableColumn<ratingprodukreport, Double> colProdRating;
    @FXML private TextField txtFilterToko;

    // Tab 2: Toko
    @FXML private TableView<ratingtokoreport> tableToko;
    @FXML private TableColumn<ratingtokoreport, Integer> colTokoId;
    @FXML private TableColumn<ratingtokoreport, Double> colTokoRating;
    @FXML private TableColumn<ratingtokoreport, Integer> colTokoFeedback;
    @FXML private TableColumn<ratingtokoreport, String> colTokoStatus;

    private reportdao reportDAO = new reportdao();
    private produkdao produkDAO = new produkdao();

    private int idTokoMilikAdmin = -1;

    @FXML
    public void initialize() {
        colProdToko.setCellValueFactory(new PropertyValueFactory<>("idToko"));
        colProdId.setCellValueFactory(new PropertyValueFactory<>("indexProduk"));
        colProdNama.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colProdTotalReview.setCellValueFactory(new PropertyValueFactory<>("totalPenilai"));
        colProdRating.setCellValueFactory(new PropertyValueFactory<>("rataRataRating"));

        colTokoId.setCellValueFactory(new PropertyValueFactory<>("idToko"));
        colTokoRating.setCellValueFactory(new PropertyValueFactory<>("ratingKeseluruhanToko"));
        colTokoFeedback.setCellValueFactory(new PropertyValueFactory<>("totalFeedbackMasuk"));
        colTokoStatus.setCellValueFactory(new PropertyValueFactory<>("statusReputasi"));


        pengguna adminAktif = usersession.getPenggunaAktif();
        if (adminAktif != null && "Admin".equalsIgnoreCase(adminAktif.getRole())) {

            idTokoMilikAdmin = produkDAO.cariIdTokoByAdmin(adminAktif.getIdpengguna());

            if (idTokoMilikAdmin == -1) {
                idTokoMilikAdmin = adminAktif.getIdpengguna();
            }
        }

        muatLaporanProduk();
        muatLaporanToko();
    }

    private void muatLaporanProduk() {
        if (idTokoMilikAdmin > 0) {
            List<ratingprodukreport> list = reportDAO.ambilReportProduk(idTokoMilikAdmin);
            ObservableList<ratingprodukreport> data = FXCollections.observableArrayList(list);
            tableProduk.setItems(data);
        }
    }

    private void muatLaporanToko() {
        if (idTokoMilikAdmin > 0) {
            List<ratingtokoreport> list = reportDAO.ambilReportToko(idTokoMilikAdmin);
            ObservableList<ratingtokoreport> data = FXCollections.observableArrayList(list);
            tableToko.setItems(data);
        }
    }

    @FXML
    private void handleCetakTransaksi(ActionEvent event) {
        pengguna userAktif = usersession.getPenggunaAktif();
        if (userAktif == null) return;

        // Memanggil fungsi cetak (tahun 2026) seperti kueri yang Anda buat
        reportDAO.cetakLaporanTransaksiUser(userAktif.getIdpengguna(), "2026-01-01", "2026-12-31");
        tampilkanPesanSukses("Laporan Transaksi", "Laporan riwayat transaksi tahun 2026 telah berhasil dicetak. Silakan periksa Terminal/Console IDE Anda.");
    }

    @FXML
    private void handleCetakPendapatan(ActionEvent event) {
        reportDAO.cetakLaporanPendapatanToko();
        tampilkanPesanSukses("Laporan Pendapatan", "Laporan peringkat pendapatan toko telah berhasil dicetak. Silakan periksa Terminal/Console IDE Anda.");
    }

    @FXML
    private void handleCetakKategori(ActionEvent event) {
        reportDAO.cetakLaporanKategoriTerlaris();
        tampilkanPesanSukses("Laporan Kategori", "Laporan tren kategori terlaris telah berhasil dicetak. Silakan periksa Terminal/Console IDE Anda.");
    }

    private void tampilkanPesanSukses(String judul, String isiPesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(judul);
        alert.setHeaderText("Cetak Sukses!");
        alert.setContentText(isiPesan);
        alert.showAndWait();
    }
}