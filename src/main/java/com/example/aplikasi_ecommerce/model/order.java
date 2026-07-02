package com.example.aplikasi_ecommerce.model;

public class order {
    // Penamaan dirapikan menggunakan aturan camelCase
    private int idOrder;
    private int idPengguna;
    private double total;
    private String statusPesanan;
    private String dateCreated; // Diubah ke String agar ramah dengan TableView JavaFX

    // Urutan parameter disamakan dengan orderdao.java
    public order(int idOrder, int idPengguna, double total, String statusPesanan, String dateCreated) {
        this.idOrder = idOrder;
        this.idPengguna = idPengguna;
        this.total = total;
        this.statusPesanan = statusPesanan;
        this.dateCreated = dateCreated;
    }

    // Getter dan Setter yang benar (Wajib persis seperti ini untuk PropertyValueFactory)
    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatusPesanan() { return statusPesanan; }
    public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}