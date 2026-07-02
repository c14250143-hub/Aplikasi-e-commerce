package com.example.aplikasi_ecommerce;

import com.example.aplikasi_ecommerce.dao.kategoridao;
import com.example.aplikasi_ecommerce.dao.keranjangdao;
import com.example.aplikasi_ecommerce.dao.produkdao;
import com.example.aplikasi_ecommerce.dao.ratingdao;
import com.example.aplikasi_ecommerce.dao.reportdao;
import com.example.aplikasi_ecommerce.model.kategori;
import com.example.aplikasi_ecommerce.model.Produk;
import com.example.aplikasi_ecommerce.model.pengguna;
import com.example.aplikasi_ecommerce.model.ratingprodukreport;
import com.example.aplikasi_ecommerce.util.usersession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class usermaincontroller implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> categoryListView;

    @FXML
    private GridPane productGrid;

    @FXML
    private MenuButton profileMenu;

    @FXML
    private Button btnadmin; // Terhubung ke tombol Dashboard Admin/Toko

    @FXML
    private ComboBox<String> comboSort;

    private List<Produk> daftarProdukSemua = new ArrayList<>();
    private List<kategori> daftarKategoriDB = new ArrayList<>();

    private produkdao produkDAO = new produkdao();
    private kategoridao kategoriDAO = new kategoridao();
    private keranjangdao keranjangDAO = new keranjangdao();
    private reportdao reportDAO = new reportdao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Muat kategori dan produk dari database
        loadKategori();
        siapkanDataProduk();

        // 2. Inisialisasi ComboBox Sorting jika terpasang di FXML
        if (comboSort != null) {
            comboSort.getItems().addAll("Terbaru", "Harga Termurah", "Harga Termahal", "Rating Tertinggi");
            comboSort.setValue("Terbaru");
            comboSort.setOnAction(e -> handleSorting());
        }

        // 3. Tambahkan filter pencarian real-time pada searchField
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                saringDanTampilkanProduk();
            });
        }

        // 4. Tambahkan filter kategori saat item di ListView diklik
        if (categoryListView != null) {
            categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                saringDanTampilkanProduk();
            });
        }

        // 5. KEAMANAN ROLE: Validasi tombol admin (btnadmin) berdasarkan sesi login
        pengguna userAktif = usersession.getPenggunaAktif();
        if (userAktif != null) {
            String role = userAktif.getRole();
            if ("Admin".equalsIgnoreCase(role)) {
                if (btnadmin != null) {
                    btnadmin.setVisible(true);
                    btnadmin.setManaged(true);
                }
            } else {
                if (btnadmin != null) {
                    btnadmin.setVisible(false);
                    btnadmin.setManaged(false);
                }
            }
        } else {
            if (btnadmin != null) {
                btnadmin.setVisible(false);
                btnadmin.setManaged(false);
            }
        }
    }

    private void loadKategori() {
        try {
            daftarKategoriDB = kategoriDAO.getAllkategori();
            ObservableList<String> namaKategori = FXCollections.observableArrayList();

            namaKategori.add("Semua Kategori");
            for (kategori k : daftarKategoriDB) {
                namaKategori.add(k.getNamakategori());
            }

            if (categoryListView != null) {
                categoryListView.setItems(namaKategori);
                categoryListView.getSelectionModel().selectFirst(); // Default select "Semua Kategori"
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat kategori ke ListView!");
            e.printStackTrace();
        }
    }

    private void siapkanDataProduk() {
        try {
            daftarProdukSemua = produkDAO.ambilsemuaproduk();

            if (daftarProdukSemua.isEmpty()) {
                System.out.println("⚠️ Database kosong atau koneksi bermasalah. Menampilkan data darurat.");
                daftarProdukSemua.add(new Produk(1, "Produk Demo (DB Kosong)", 25000, 10, "Deskripsi produk demo yang sangat panjang dan detail.", 1, 1, null));
            }

            tampilkanProdukKeGrid(daftarProdukSemua);
        } catch (Exception e) {
            System.err.println("Gagal menyiapkan data produk!");
            e.printStackTrace();
        }
    }

    private void saringDanTampilkanProduk() {
        if (daftarProdukSemua == null || daftarProdukSemua.isEmpty()) return;

        String teksCari = searchField != null ? searchField.getText().toLowerCase().trim() : "";
        String kategoriTerpilih = categoryListView != null ? categoryListView.getSelectionModel().getSelectedItem() : "Semua Kategori";

        List<Produk> listHasilSaring = daftarProdukSemua.stream()
                .filter(p -> {
                    boolean cocokNama = p.getNamaProduk().toLowerCase().contains(teksCari);
                    boolean cocokKategori = true;
                    if (kategoriTerpilih != null && !kategoriTerpilih.equals("Semua Kategori")) {
                        int idKategoriTerpilih = -1;
                        for (kategori k : daftarKategoriDB) {
                            if (k.getNamakategori().equals(kategoriTerpilih)) {
                                idKategoriTerpilih = k.getIdkategori();
                                break;
                            }
                        }
                        cocokKategori = (p.getIdkategori() == idKategoriTerpilih);
                    }
                    return cocokNama && cocokKategori;
                })
                .collect(Collectors.toList());

        tampilkanProdukKeGrid(listHasilSaring);
    }

    private void handleSorting() {
        if (comboSort == null || daftarProdukSemua.isEmpty()) return;
        String pilihan = comboSort.getValue();

        List<Produk> listUrut = new ArrayList<>(daftarProdukSemua);
        List<ratingprodukreport> dataRating = reportDAO.ambilReportProduk(0);

        if ("Harga Termurah".equals(pilihan)) {
            listUrut.sort((p1, p2) -> Double.compare(p1.getHargaProduk(), p2.getHargaProduk()));
        } else if ("Harga Termahal".equals(pilihan)) {
            listUrut.sort((p1, p2) -> Double.compare(p2.getHargaProduk(), p1.getHargaProduk()));
        } else if ("Rating Tertinggi".equals(pilihan)) {
            listUrut.sort((p1, p2) -> {
                double r1 = dapatkanRataRataRating(p1.getIdProduk(), dataRating);
                double r2 = dapatkanRataRataRating(p2.getIdProduk(), dataRating);
                return Double.compare(r2, r1);
            });
        }
        tampilkanProdukKeGrid(listUrut);
    }

    private double dapatkanRataRataRating(int idProduk, List<ratingprodukreport> dataRating) {
        for (ratingprodukreport r : dataRating) {
            if (r.getIndexProduk() == idProduk) {
                return r.getRataRataRating();
            }
        }
        return 0.0;
    }

    private void tampilkanProdukKeGrid(List<Produk> list) {
        if (productGrid == null) return;
        productGrid.getChildren().clear();

        int kolom = 0;
        int baris = 0;

        List<ratingprodukreport> dataRating = reportDAO.ambilReportProduk(0);

        for (Produk p : list) {
            VBox card = buatProductCard(p, dataRating);
            productGrid.add(card, kolom, baris);

            kolom++;
            if (kolom > 2) {
                kolom = 0;
                baris++;
            }
        }
    }

    private VBox buatProductCard(Produk p, List<ratingprodukreport> dataRating) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(240);

        Label lblNama = new Label(p.getNamaProduk());
        lblNama.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        lblNama.setWrapText(true);

        Label lblHarga = new Label(String.format("Rp %,.2f", p.getHargaProduk()));
        lblHarga.setStyle("-fx-font-size: 13px; -fx-text-fill: #ff5722; -fx-font-weight: bold;");

        // Ambil rating ulasan rata-rata
        double avgRating = 0;
        int totalUlasan = 0;
        for (ratingprodukreport r : dataRating) {
            if (r.getIndexProduk() == p.getIdProduk()) {
                avgRating = r.getRataRataRating();
                totalUlasan = r.getTotalPenilai();
                break;
            }
        }

        Label lblRating = new Label();
        if (totalUlasan > 0) {
            lblRating.setText(String.format("⭐ %.1f (%d Ulasan)", avgRating, totalUlasan));
            lblRating.setStyle("-fx-font-size: 11px; -fx-text-fill: #ff9800; -fx-font-weight: bold;");
        } else {
            lblRating.setText("⭐ Belum ada ulasan");
            lblRating.setStyle("-fx-font-size: 11px; -fx-text-fill: #9e9e9e;");
        }

        Label lblStok = new Label("Stok: " + p.getStok());
        lblStok.setStyle("-fx-font-size: 11px; -fx-text-fill: #757575;");

        // --- TOMBOL INTERAKTIF (DETAIL & BELI) ---
        final double finalAvg = avgRating;
        final int finalTotal = totalUlasan;

        Button btnDetail = new Button("ℹ️ Detail");
        btnDetail.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        btnDetail.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnDetail, javafx.scene.layout.Priority.ALWAYS);
        btnDetail.setOnAction(e -> handleLihatDetail(p, finalAvg, finalTotal));

        Button btnBeli = new Button("🛒 Beli");
        btnBeli.setStyle("-fx-background-color: #0d6efd; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        btnBeli.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnBeli, javafx.scene.layout.Priority.ALWAYS);
        btnBeli.setOnAction(e -> handleTambahKeKeranjang(p));

        HBox barTombol = new HBox(6, btnDetail, btnBeli);
        barTombol.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(lblNama, lblHarga, lblRating, lblStok, barTombol);
        return card;
    }

    // =========================================================
    // FITUR UTAMA: MELIHAT DESKRIPSI LENGKAP & RATING ORANG LAIN
    // =========================================================
    private void handleLihatDetail(Produk p, double avgRating, int totalUlasan) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Detail & Ulasan Produk");
        dialog.setHeaderText(p.getNamaProduk());

        // Ambil ulasan/rating teks langsung dari database PostgreSQL
        List<String> ulasanOrangLain = dapatkanUlasanUlasanDatabase(p.getIdProduk());

        StringBuilder ulasanStr = new StringBuilder();
        if (ulasanOrangLain.isEmpty()) {
            ulasanStr.append("Belum ada ulasan tertulis untuk produk ini.");
        } else {
            for (String review : ulasanOrangLain) {
                ulasanStr.append(review).append("\n──────────────────────\n");
            }
        }

        // Siapkan text rating
        String ratingText = totalUlasan > 0 ?
                String.format("⭐ %.1f / 5.0 dari %d Ulasan", avgRating, totalUlasan) :
                "⭐ Belum ada ulasan untuk produk ini.";

        // Susun detail info
        String isiDetail = "Harga Satuan : Rp " + String.format("%,.2f", p.getHargaProduk()) + "\n"
                + "Stok Tersedia: " + p.getStok() + " unit\n"
                + "Skor Rating  : " + ratingText + "\n\n"
                + "===== DESKRIPSI PRODUK =====\n"
                + (p.getDesk_produk() != null && !p.getDesk_produk().isEmpty() ? p.getDesk_produk() : "Tidak ada deskripsi.") + "\n\n"
                + "===== ULASAN & RATING PEMBELI =====\n"
                + ulasanStr.toString();

        // Menggunakan TextArea agar deskripsi panjang dan ulasan bisa di-scroll dengan rapi
        TextArea txtArea = new TextArea(isiDetail);
        txtArea.setEditable(false);
        txtArea.setWrapText(true);
        txtArea.setPrefHeight(320);
        txtArea.setPrefWidth(420);

        dialog.getDialogPane().setContent(txtArea);
        dialog.showAndWait();
    }

    // Helper penarik ulasan & rating pembeli lain langsung menggunakan query JDBC aman
    private List<String> dapatkanUlasanUlasanDatabase(int idProduk) {
        List<String> listUlasan = new ArrayList<>();

        // Query untuk mencari rating bintang dan mencocokkan dengan nama pembelinya
        String query = "SELECT p.nama, r.nilai_bintang " +
                "FROM rating r " +
                "JOIN pengguna p ON r.id_pengguna = p.id_pengguna " +
                "WHERE r.id_produk = ?";

        try (Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idProduk);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String namaPembeli = rs.getString("nama");
                    int bintang = rs.getInt("nilai_bintang");

                    StringBuilder stars = new StringBuilder();
                    for (int i = 0; i < bintang; i++) {
                        stars.append("⭐");
                    }
                    listUlasan.add("👤 " + namaPembeli + " \nRating: " + stars.toString() + " (" + bintang + "/5 bintang)");
                }
            }
        } catch (Exception e) {
            // Fallback aman jika terdapat perbedaan struktur tabel pengguna
            String fallbackQuery = "SELECT id_pengguna, nilai_bintang FROM rating WHERE id_produk = ?";
            try (Connection conn = com.example.aplikasi_ecommerce.util.databaseconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(fallbackQuery)) {

                stmt.setInt(1, idProduk);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int idUser = rs.getInt("id_pengguna");
                        int bintang = rs.getInt("nilai_bintang");

                        StringBuilder stars = new StringBuilder();
                        for (int i = 0; i < bintang; i++) {
                            stars.append("⭐");
                        }
                        listUlasan.add("👤 Pembeli #" + idUser + " \nRating: " + stars.toString());
                    }
                }
            } catch (Exception ex) {
                System.err.println("Gagal menarik rating detail dari database: " + ex.getMessage());
            }
        }
        return listUlasan;
    }

    private void handleTambahKeKeranjang(Produk p) {
        pengguna user = usersession.getPenggunaAktif();
        if (user == null) {
            tampilkanAlert(Alert.AlertType.WARNING, "Login Diperlukan", "Silakan masuk ke akun Anda terlebih dahulu!");
            return;
        }

        if (p.getStok() <= 0) {
            tampilkanAlert(Alert.AlertType.ERROR, "Stok Habis", "Maaf, stok produk \"" + p.getNamaProduk() + "\" sudah habis.");
            return;
        }

        boolean sukses = keranjangDAO.tambahkeranjang(user.getIdpengguna(), p.getIdProduk(), 1);
        if (sukses) {
            tampilkanAlert(Alert.AlertType.INFORMATION, "Sukses", "\"" + p.getNamaProduk() + "\" ditambahkan ke keranjang belanja!");
        } else {
            tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kegagalan saat memasukkan barang ke keranjang.");
        }
    }

    // =========================================================
    // OPERASI NAVIGASI HALAMAN (ROUTING)
    // =========================================================
    @FXML
    private void goToKeranjang(javafx.event.ActionEvent event) {
        pindahHalaman(event, "keranjang.fxml");
    }

    @FXML
    private void goToRiwayat(javafx.event.ActionEvent event) {
        pindahHalaman(event, "order_history.fxml");
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        usersession.bersihkanSesi();
        pindahHalaman(event, "login.fxml");
    }

    @FXML
    private void bukahalamanadmin(javafx.event.ActionEvent event) {
        pengguna user = usersession.getPenggunaAktif();
        if (user != null) {
            if (!"Admin".equalsIgnoreCase(user.getRole())) {
                tampilkanAlert(Alert.AlertType.ERROR, "Akses Ditolak", "Anda tidak memiliki wewenang untuk masuk ke halaman ini.");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_dasboard.fxml"));
            Parent root = loader.load();

            Stage adminStage = new Stage();
            adminStage.setTitle("Sistem Manajemen Toko - Dashboard Admin");
            adminStage.setScene(new Scene(root));
            adminStage.initModality(Modality.APPLICATION_MODAL);
            adminStage.centerOnScreen();

            adminStage.setOnCloseRequest(e -> siapkanDataProduk());
            adminStage.show();

        } catch (IOException e) {
            System.err.println("Gagal memuat file admin_dasboard.fxml!");
            e.printStackTrace();
            tampilkanAlert(Alert.AlertType.ERROR, "Sistem Error", "Halaman Dashboard Admin tidak dapat dimuat.");
        }
    }

    private void pindahHalaman(javafx.event.ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanAlert(Alert.AlertType.ERROR, "Sistem Error", "Gagal memuat halaman: " + fxmlPath);
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