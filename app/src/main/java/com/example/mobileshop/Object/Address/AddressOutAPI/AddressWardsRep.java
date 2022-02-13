package com.example.mobileshop.Object.Address.AddressOutAPI;

import java.util.ArrayList;

public class AddressWardsRep {
    int code;
    String message;
    ArrayList<AddressWardsData> data;

    public AddressWardsRep(int code, String message, ArrayList<AddressWardsData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AddressWardsData> getData() {
        return data;
    }

    public void setData(ArrayList<AddressWardsData> data) {
        this.data = data;
    }
}
