package com.example.mobileshop.Object.ProductDetail;

import java.io.Serializable;

public class ProductCommentsDetail implements Serializable {
    String commentText;
    String datecomment;

    public ProductCommentsDetail(String commentText, String datecomment) {
        this.commentText = commentText;
        this.datecomment = datecomment;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDatecomment() {
        return datecomment;
    }

    public void setDatecomment(String datecomment) {
        this.datecomment = datecomment;
    }
}
