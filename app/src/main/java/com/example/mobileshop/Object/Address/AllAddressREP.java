package com.example.mobileshop.Object.Address;

import java.util.ArrayList;

public class AllAddressREP {
    int status;
    String notification;
    ArrayList<Address> addressList ;

    public AllAddressREP(int status, String notification, ArrayList<Address> addressList) {
        this.status = status;
        this.notification = notification;
        this.addressList = addressList;
    }

    public AllAddressREP() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }
}
