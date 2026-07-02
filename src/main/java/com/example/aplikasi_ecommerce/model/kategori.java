package com.example.aplikasi_ecommerce.model;

public class kategori {
    private int idkategori;
    private String namakategori;

    public kategori(int idkategori, String namakategori) {
        this.idkategori = idkategori;
        this.namakategori = namakategori;
    }

    public int getIdkategori() { return idkategori; }
    public void setIdkategori(int idkategori) { this.idkategori = idkategori; }

    public String getNamakategori() { return namakategori; }
    public void setNamakategori(String namakategori) { this.namakategori = namakategori; }
}