package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.penggunadao;
import com.example.aplikasi_ecommerce.model.pengguna;
import com.example.aplikasi_ecommerce.util.usersession;
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

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // 1. Validasi input kosong
        if (email.isEmpty() || password.isEmpty()) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Email dan Password tidak boleh kosong!");
            return;
        }

        // 2. Cek ke database
        penggunadao dao = new penggunadao();
        pengguna userAktif = dao.autentikasiLogin(email, password);


        if (userAktif != null) {
            com.example.aplikasi_ecommerce.util.usersession.setpenggunaAktif(userAktif);

            String role = userAktif.getRole(); // Mengambil role dari object yang berhasil login
            if (role.equalsIgnoreCase("Admin")) {
                pindahHalaman(event, "uiinterface.fxml");
            } else if (role.equalsIgnoreCase("Customer")) {
                pindahHalaman(event, "uiinterface.fxml");

            }

        } else {
            tampilkanAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau Password salah. Silakan coba lagi.");
        }
    }

    @FXML
    private void goToRegister(MouseEvent event) {
        pindahHalamanMouse(event, "register.fxml");
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Fungsi serbaguna untuk pindah halaman via tombol (ActionEvent)
    private void pindahHalaman(ActionEvent event, String fxmlTujuan) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlTujuan));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman: " + fxmlTujuan);
        }
    }

    // Fungsi serbaguna untuk pindah halaman via klik teks (MouseEvent)
    private void pindahHalamanMouse(MouseEvent event, String fxmlTujuan) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlTujuan));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}