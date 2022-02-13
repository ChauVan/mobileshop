package com.example.mobileshop.Object.HomeAllProduct;

import java.util.ArrayList;

public class HomeAllProductMessage {
    int status;
    String notification;
    ArrayList<HomeAllProduct> products;

    public HomeAllProductMessage(int status, String notification, ArrayList<HomeAllProduct> products) {
        this.status = status;
        this.notification = notification;
        this.products = products;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<HomeAllProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<HomeAllProduct> products) {
        this.products = products;
    }
}
