package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.ratingdao;
import com.example.aplikasi_ecommerce.model.rating;
import com.example.aplikasi_ecommerce.util.usersession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class controllerrating {

    @FXML private Label lblNamaProduk;
    @FXML private ComboBox<Integer> comboBintang;

    private int idProdukTerpilih;
    private String namaProdukTerpilih;
    private ratingdao ratingDAO = new ratingdao();

    @FXML
    public void initialize() {
        // Isi ComboBox dengan pilihan rating bintang 1 hingga 5
        comboBintang.getItems().addAll(1, 2, 3, 4, 5);
        comboBintang.setValue(5); // Default 5 Bintang
    }

    public void setProdukData(int idProduk, String namaProduk) {
        this.idProdukTerpilih = idProduk;
        this.namaProdukTerpilih = namaProduk;
        lblNamaProduk.setText("Berikan Ulasan Anda untuk:\n" + namaProduk);
    }

    @FXML
    private void handleSimpanRating(ActionEvent event) {
        if (usersession.getPenggunaAktif() == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Anda harus login terlebih dahulu.");
            return;
        }

        int idUser = usersession.getPenggunaAktif().getIdpengguna();
        int bintangSelected = comboBintang.getValue();

        // Buat objek rating baru (id_rating diset 0 karena auto-increment di PostgreSQL)
        rating ratingBaru = new rating(0, bintangSelected, idUser, idProdukTerpilih);

        boolean sukses = ratingDAO.tambahRating(ratingBaru);

        if (sukses) {
            tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Terima kasih! Ulasan bintang " + bintangSelected + " Anda telah terekam.");
            // Tutup window popup
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan ulasan. Anda mungkin sudah pernah menilai produk ini!");
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}