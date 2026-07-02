package com.example.aplikasi_ecommerce.model;

public class itemkeranjang {
    private int idKeranjang;
    private int indexProduk;
    private String namaProduk;
    private double hargaProduk;
    private int jumlah;
    private double subtotal;

    public itemkeranjang(int idKeranjang, int indexProduk, String namaProduk, double hargaProduk, int jumlah) {
        this.idKeranjang = idKeranjang;
        this.indexProduk = indexProduk;
        this.namaProduk = namaProduk;
        this.hargaProduk = hargaProduk;
        this.jumlah = jumlah;
        this.subtotal = hargaProduk * jumlah;
    }


    public int getIdKeranjang() {
        return idKeranjang;
    }

    public void setIdKeranjang(int idKeranjang) {
        this.idKeranjang = idKeranjang;
    }

    public int getIndexProduk() {
        return indexProduk;
    }

    public void setIndexProduk(int indexProduk) {
        this.indexProduk = indexProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public double getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(double hargaProduk) {
        this.hargaProduk = hargaProduk;
        this.subtotal = this.hargaProduk * this.jumlah; // Rekalkulasi otomatis subtotal jika harga berubah
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        this.subtotal = this.hargaProduk * jumlah;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}

