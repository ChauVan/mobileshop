package com.example.mobileshop.Object.Address.AddressOutAPI;

import java.util.ArrayList;

public class AddressProvinceREP {
    int code;
    String message;
    ArrayList<AddressProvinceData> data ;

    public AddressProvinceREP(int code, String message, ArrayList<AddressProvinceData> data) {
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

    public ArrayList<AddressProvinceData> getData() {
        return data;
    }

    public void setData(ArrayList<AddressProvinceData> data) {
        this.data = data;
    }
}
