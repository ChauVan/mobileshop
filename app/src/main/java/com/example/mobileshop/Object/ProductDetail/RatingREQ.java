package com.example.mobileshop.Object.ProductDetail;

public class RatingREQ {
    String user;
    int productId;
    int rate ;
    String comment;

    public RatingREQ(String user, int productId, int rate, String comment) {
        this.user = user;
        this.productId = productId;
        this.rate = rate;
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
