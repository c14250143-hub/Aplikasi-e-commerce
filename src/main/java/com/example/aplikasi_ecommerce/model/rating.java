package com.example.aplikasi_ecommerce.model;

public class rating {
    private int idRating;
    private int nilaiBintang;
    private int idPengguna;
    private int idProduk;

    public rating(int idRating, int nilaiBintang, int idPengguna, int idProduk) {
        this.idRating = idRating;
        this.nilaiBintang = nilaiBintang;
        this.idPengguna = idPengguna;
        this.idProduk = idProduk;
    }

    public int getIdRating() { return idRating; }
    public void setIdRating(int idRating) { this.idRating = idRating; }

    public int getNilaiBintang() { return nilaiBintang; }
    public void setNilaiBintang(int nilaiBintang) { this.nilaiBintang = nilaiBintang; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdProduk() { return idProduk; }
    public void setIdProduk(int idProduk) { this.idProduk = idProduk; }
}
