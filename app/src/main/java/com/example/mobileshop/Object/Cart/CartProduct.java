package com.example.mobileshop.Object.Cart;

import java.io.Serializable;

public class CartProduct implements Serializable {
    int productId;
    String pic,productName,productStatus;
    int amount;
    long price;

    public CartProduct(int productId, String pic, String productName, String productStatus, int amount, long price) {
        this.productId = productId;
        this.pic = pic;
        this.productName = productName;
        this.productStatus = productStatus;
        this.amount = amount;
        this.price = price;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
