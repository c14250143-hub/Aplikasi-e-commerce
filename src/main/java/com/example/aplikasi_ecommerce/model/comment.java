package com.example.aplikasi_ecommerce.model;

import java.time.LocalDateTime;

public class comment {
    private int idComment;
    private String isiKomentar;
    private LocalDateTime tanggalKomentar;
    private int idPengguna;
    private int idProduk;

    public comment(int idComment, String isiKomentar, LocalDateTime tanggalKomentar, int idPengguna, int idProduk) {
        this.idComment = idComment;
        this.isiKomentar = isiKomentar;
        this.tanggalKomentar = tanggalKomentar;
        this.idPengguna = idPengguna;
        this.idProduk = idProduk;
    }

    // Getter dan Setter
    public int getIdComment() { return idComment; }
    public void setIdComment(int idComment) { this.idComment = idComment; }

    public String getIsiKomentar() { return isiKomentar; }
    public void setIsiKomentar(String isiKomentar) { this.isiKomentar = isiKomentar; }

    public LocalDateTime getTanggalKomentar() { return tanggalKomentar; }
    public void setTanggalKomentar(LocalDateTime tanggalKomentar) { this.tanggalKomentar = tanggalKomentar; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdProduk() { return idProduk; }
    public void setIdProduk(int idProduk) { this.idProduk = idProduk; }
}
