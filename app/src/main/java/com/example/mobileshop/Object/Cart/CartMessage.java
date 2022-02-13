package com.example.mobileshop.Object.Cart;

import java.util.ArrayList;

public class CartMessage {
    int status;
    String notification;
    ArrayList<CartProduct> cart;

    public CartMessage(int status, String notification, ArrayList<CartProduct> cart) {
        this.status = status;
        this.notification = notification;
        this.cart = cart;
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

    public ArrayList<CartProduct> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartProduct> cart) {
        this.cart = cart;
    }
}
