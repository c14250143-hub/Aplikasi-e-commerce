package com.example.aplikasi_ecommerce.model;
public class Produk {
    private int idProduk;
    private String namaProduk;
    private double hargaProduk;
    private int stok;
    private String desk_produk;
    private int idtoko;
    private int idkategori;
    private String imagePath;

    public Produk(int idProduk, String namaProduk, double hargaProduk, int stok, String desk_produk, int idtoko, int idkategori, String imagePath) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.hargaProduk = hargaProduk;
        this.stok = stok;
        this.desk_produk = desk_produk;
        this.idtoko = idtoko;
        this.idkategori = idkategori;
        this.imagePath = imagePath;
    }

    public int getIdProduk() {

        return idProduk;
    }

    public String getNamaProduk() {

        return namaProduk;
    }

    public double getHargaProduk() {

        return hargaProduk;
    }

    public int getStok() {
        return stok;
    }

    public int getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(int idtoko) {
        this.idtoko = idtoko;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public String getImagePath() {

        return imagePath;
    }

    public String getDesk_produk() {
        return desk_produk;
    }

    public void setDesk_produk(String desk_produk) {
        this.desk_produk = desk_produk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public void setHargaProduk(double hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
