package com.example.mobileshop.Object.AddCart;

public class CartAdd {
    String user;
    String productID;
    Boolean status;
    int amount;

    public CartAdd(String user, String productID, Boolean status, int amount) {
        this.user = user;
        this.productID = productID;
        this.status = status;
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
