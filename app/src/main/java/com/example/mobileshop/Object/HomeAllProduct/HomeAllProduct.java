package com.example.mobileshop.Object.HomeAllProduct;

import java.util.ArrayList;

public class HomeAllProduct {
    private int id;
    private String name;
    private String picMain;
    private long price;
    private int totalrate;
    private int countrate;
    private int totalPay;
    ArrayList<String> listPic;

    public HomeAllProduct(int id, String name, String picMain, long price, int totalrate, int countrate, int totalPay, ArrayList<String> listPic) {
        this.id = id;
        this.name = name;
        this.picMain = picMain;
        this.price = price;
        this.totalrate = totalrate;
        this.countrate = countrate;
        this.listPic = listPic;
        this.totalPay = totalPay;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicMain() {
        return picMain;
    }

    public void setPicMain(String picMain) {
        this.picMain = picMain;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(int totalrate) {
        this.totalrate = totalrate;
    }

    public int getCountrate() {
        return countrate;
    }

    public void setCountrate(int countrate) {
        this.countrate = countrate;
    }

    public ArrayList<String> getListPic() {
        return listPic;
    }

    public void setListPic(ArrayList<String> listPic) {
        this.listPic = listPic;
    }
}
