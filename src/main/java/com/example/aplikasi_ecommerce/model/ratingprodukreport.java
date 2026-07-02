package com.example.aplikasi_ecommerce.model;

public class ratingprodukreport {
    private int idToko;
    private int indexProduk;
    private String namaProduk;
    private int totalPenilai;
    private double rataRataRating;

    public ratingprodukreport(int idToko, int indexProduk, String namaProduk, int totalPenilai, double rataRataRating) {
        this.idToko = idToko;
        this.indexProduk = indexProduk;
        this.namaProduk = namaProduk;
        this.totalPenilai = totalPenilai;
        this.rataRataRating = rataRataRating;
    }

    public int getIdToko() { return idToko; }
    public int getIndexProduk() { return indexProduk; }
    public String getNamaProduk() { return namaProduk; }
    public int getTotalPenilai() { return totalPenilai; }
    public double getRataRataRating() { return rataRataRating; }
}
