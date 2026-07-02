package com.example.aplikasi_ecommerce.model;

public class toko {
    private int idToko;
    private int idPengguna;
    private String deskToko;

    public toko(int idToko, int idPengguna, String deskToko) {
        this.idToko = idToko;
        this.idPengguna = idPengguna;
        this.deskToko = deskToko;
    }

    public int getIdToko() { return idToko; }
    public void setIdToko(int idToko) { this.idToko = idToko; }

    public int getIdPengguna() { return idPengguna; }
    public void setIdPengguna(int idPengguna) { this.idPengguna = idPengguna; }

    public String getDeskToko() { return deskToko; }
    public void setDeskToko(String deskToko) { this.deskToko = deskToko; }
}

