package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.orderdao;
import com.example.aplikasi_ecommerce.model.order;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

public class orderhistorycontroller {
    @FXML private TableView<order> tabelRiwayat;
    @FXML private TableColumn<order, Integer> colIdOrder;
    @FXML private TableColumn<order, LocalDateTime> colTanggal;
    @FXML private TableColumn<order, Double> colTotal;
    @FXML private TableColumn<order, String> colStatus;

    private orderdao orderDAO = new orderdao();

    @FXML
    public void initialize() {
        colIdOrder.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusPesanan"));

        muatRiwayatMulai();
    }

    private void muatRiwayatMulai() {
        if (usersession.getPenggunaAktif() != null) {
            int idUser = usersession.getPenggunaAktif().getIdpengguna();
            List<order> list = orderDAO.ambilRiwayatUser(idUser);

            ObservableList<order> obList = FXCollections.observableArrayList(list);
            tabelRiwayat.setItems(obList);
        }
    }

    @FXML
    private void handleBeriRating(ActionEvent event) {
        order pesananTerpilih = tabelRiwayat.getSelectionModel().getSelectedItem();

        if (pesananTerpilih == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Pilih Pesanan", "Silakan pilih pesanan yang ingin diberi ulasan pada tabel.");
            return;
        }

        // Validasi: Hanya pesanan Selesai / Dikirim yang bisa dirating
        String status = pesananTerpilih.getStatusPesanan();
        if (!status.equalsIgnoreCase("Selesai") && !status.equalsIgnoreCase("Dikirim")) {
            tampilkanAlert(Alert.AlertType.WARNING, "Belum Selesai", "Rating hanya bisa diberikan untuk pesanan yang sudah dikirim atau selesai.");
            return;
        }

        int idProduk = -1;
        String namaProduk = "Produk Pesanan #" + pesananTerpilih.getIdOrder();

        String queryCariProduk = "SELECT p.index_produk, p.nama_produk FROM order_detail od " +
                "JOIN produk p ON od.index_produk = p.index_produk " +
                "WHERE od.id_order = ? LIMIT 1";

        try (Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryCariProduk)) {

            stmt.setInt(1, pesananTerpilih.getIdOrder());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idProduk = rs.getInt("index_produk");
                    namaProduk = rs.getString("nama_produk");
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal menarik detail produk untuk rating.");
            e.printStackTrace();
        }

        if (idProduk != -1) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("rating.fxml"));
                Parent root = loader.load();

                controllerrating controller = loader.getController();
                controller.setProdukData(idProduk, namaProduk);

                Stage stage = new Stage();
                stage.setTitle("Beri Ulasan");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait(); // Tunggu sampai popup ditutup

            } catch (IOException e) {
                e.printStackTrace();
                tampilkanAlert(Alert.AlertType.ERROR, "Sistem Error", "Gagal memuat jendela rating.");
            }
        } else {
            tampilkanAlert(Alert.AlertType.ERROR, "Error", "Tidak dapat menemukan detail produk untuk pesanan ini.");
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("uiinterface.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
