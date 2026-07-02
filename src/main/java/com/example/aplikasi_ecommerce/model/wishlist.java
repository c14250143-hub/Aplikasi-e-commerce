package com.example.aplikasi_ecommerce.model;

public class wishlist {
    private int idWishlist;
    private int idPengguna;
    private int idProduk;

    public wishlist(int idWishlist, int idPengguna, int idProduk) {
        this.idWishlist = idWishlist;
        this.idPengguna = idPengguna;
        this.idProduk = idProduk;
    }

    public int getIdWishlist() { return idWishlist; }
    public void setIdWishlist(int idWishlist) { this.idWishlist = idWishlist; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public int getIdProduk() { return idProduk; }
    public void setIdProduk(int idProduk) { this.idProduk = idProduk; }
}
