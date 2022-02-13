package com.example.mobileshop.Object.Cart;

public class CartChangeProduct {
    private int productId;
    private String status;
    private int amount;

    public CartChangeProduct(int productId, String status, int amount) {
        this.productId = productId;
        this.status = status;
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
