package com.example.aplikasi_ecommerce.model;

public class ratingtokoreport {
    private int idToko;
    private double ratingKeseluruhanToko;
    private int totalFeedbackMasuk;
    private String statusReputasi;

    public ratingtokoreport(int idToko, double ratingKeseluruhanToko, int totalFeedbackMasuk, String statusReputasi) {
        this.idToko = idToko;
        this.ratingKeseluruhanToko = ratingKeseluruhanToko;
        this.totalFeedbackMasuk = totalFeedbackMasuk;
        this.statusReputasi = statusReputasi;
    }

    public int getIdToko() { return idToko; }
    public double getRatingKeseluruhanToko() { return ratingKeseluruhanToko; }
    public int getTotalFeedbackMasuk() { return totalFeedbackMasuk; }
    public String getStatusReputasi() { return statusReputasi; }
}
