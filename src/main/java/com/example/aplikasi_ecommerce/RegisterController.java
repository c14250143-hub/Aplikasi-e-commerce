package com.example.aplikasi_ecommerce;


import com.example.aplikasi_ecommerce.dao.penggunadao;
import com.example.aplikasi_ecommerce.model.pengguna;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private TextField teleponField;
    @FXML private TextField alamatField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String nama = namaField.getText();
        String email = emailField.getText();
        String telepon = teleponField.getText();
        String alamat = alamatField.getText();
        String password = passwordField.getText();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || password.isEmpty()) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Semua kolom harus diisi!");
            return;
        }

        pengguna penggunaBaru = new pengguna(0, nama, email, password, alamat, telepon, "Customer");

        penggunadao dao = new penggunadao();
        boolean sukses = dao.insertpengguna(penggunaBaru);

        // 4. Berikan respon ke pengguna
        if (sukses) {
            tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "Akun berhasil dibuat! Silakan login.");
            kembaliKeLogin(event); // Langsung pindah ke halaman login
        } else {
            tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat mendaftar. Email mungkin sudah digunakan.");
        }
    }

    @FXML
    private void goToLogin(MouseEvent event) {
        kembaliKeLogin(event);
    }

    private void kembaliKeLogin(javafx.event.Event event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}