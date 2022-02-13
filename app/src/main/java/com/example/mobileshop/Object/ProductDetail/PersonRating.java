package com.example.mobileshop.Object.ProductDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonRating implements Serializable {
    String userName;
    int rate;
    ArrayList<ProductCommentsDetail> productComments;

    public PersonRating(String userName, int rate, ArrayList<ProductCommentsDetail> productComments) {
        this.userName = userName;
        this.rate = rate;
        this.productComments = productComments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<ProductCommentsDetail> getProductComments() {
        return productComments;
    }

    public void setProductComments(ArrayList<ProductCommentsDetail> productComments) {
        this.productComments = productComments;
    }
}
