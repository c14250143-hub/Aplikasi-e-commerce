package com.example.aplikasi_ecommerce;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class admindashboardcontroller {

    @FXML
    private void bukaKelolaProduk(ActionEvent event) {
        bukaJendelaBaru("kelola_produk.fxml", "Kelola Produk - Admin");
    }

    @FXML
    private void bukaReportInsight(ActionEvent event) {
        bukaJendelaBaru("report_insight.fxml", "Laporan Business Insight - Analytics");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bukaJendelaBaru(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman: " + fxmlFile);
            e.printStackTrace();
        }
    }
    @FXML
    private void bukaorderstatus(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("order_status_check.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Manajemen Pesanan Masuk");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}