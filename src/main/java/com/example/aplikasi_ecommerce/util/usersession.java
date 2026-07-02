package com.example.aplikasi_ecommerce.util;

import com.example.aplikasi_ecommerce.model.pengguna;

public class usersession {
    private static pengguna penggunaAktif;

    public static void setpenggunaAktif(pengguna pengguna) {
        penggunaAktif = pengguna;
    }

    public static pengguna getPenggunaAktif() {
        return penggunaAktif;
    }

    public static void bersihkanSesi() {
        penggunaAktif = null;
    }
}