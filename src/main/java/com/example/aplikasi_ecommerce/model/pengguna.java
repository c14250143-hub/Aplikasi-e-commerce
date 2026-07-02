package com.example.aplikasi_ecommerce.model;

public class pengguna {
    private int idpengguna;
    private String nama;
    private String email;
    private String password;
    private String alamat;
    private String telephone;
    private String role;

    // Konstruktor disesuaikan untuk menerima 7 parameter (termasuk role)
    public pengguna(int idpengguna, String nama, String email, String password, String alamat, String telephone, String role) {
        this.idpengguna = idpengguna;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
        this.telephone = telephone;
        this.role = role;
    }

    // ================== GETTER & SETTER ==================
    public int getIdpengguna() { return idpengguna; }
    public void setIdpengguna(int idpengguna) { this.idpengguna = idpengguna; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    // Getter dan Setter untuk Role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}