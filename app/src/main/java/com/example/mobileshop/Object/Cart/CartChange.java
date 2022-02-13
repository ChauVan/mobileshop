package com.example.mobileshop.Object.Cart;

import java.util.ArrayList;

public class CartChange {
    private String user;
    private ArrayList<CartChangeProduct> products;

    public CartChange(String user, ArrayList<CartChangeProduct> products) {
        this.user = user;
        this.products = products;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<CartChangeProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<CartChangeProduct> products) {
        this.products = products;
    }
}
