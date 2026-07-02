package com.example.aplikasi_ecommerce.model;

public class orderdetail {
    private int idorderdetail;
    private int qty;
    private double subTotal;
    private int idOrder;
    private int indexProduk;

    // <--- TAMBAHAN WAJIB UNTUK UI NOTA PESANAN --->
    private String namaProduk;
    private double hargaSatuan;

    public orderdetail(int idorderdetail, int qty, double subTotal, int idOrder, int indexProduk, String namaProduk, double hargaSatuan) {
        this.idorderdetail = idorderdetail;
        this.qty = qty;
        this.subTotal = subTotal;
        this.idOrder = idOrder;
        this.indexProduk = indexProduk;
        this.namaProduk = namaProduk;
        this.hargaSatuan = hargaSatuan;
    }

    // ================== GETTER & SETTER ==================
    public int getIdorderdetail() { return idorderdetail; }
    public void setIdorderdetail(int idorderdetail) { this.idorderdetail = idorderdetail; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public int getIndexProduk() { return indexProduk; }
    public void setIndexProduk(int indexProduk) { this.indexProduk = indexProduk; }

    // Getter dan Setter Baru
    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
}
