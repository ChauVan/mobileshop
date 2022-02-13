package com.example.mobileshop.Object.CheckOut;

public class CheckOutREQ {
    private String user;
    private int addressId;
    private String note;

    public CheckOutREQ(String user, int addressId, String note) {
        this.user = user;
        this.addressId = addressId;
        this.note = note;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
