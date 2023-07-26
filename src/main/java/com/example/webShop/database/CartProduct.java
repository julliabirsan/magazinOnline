package com.example.webShop.database;

public class CartProduct extends Product{
    private int cantitate;
    private double pretTotal;

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public double getPretTotal() {
        return pretTotal;
    }

    public void setPretTotal(double pretTotal) {
        this.pretTotal = pretTotal;
    }
}
