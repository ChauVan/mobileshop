package com.example.mobileshop.Object.ProductDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class Rating implements Serializable {
    ArrayList<PersonRating> ratings;

    public Rating(ArrayList<PersonRating> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<PersonRating> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<PersonRating> ratings) {
        this.ratings = ratings;
    }
}
