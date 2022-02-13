package com.example.mobileshop.Object.Address.AddressOutAPI;

import java.util.ArrayList;

public class AddressDistrictRep {
    int DistrictID;
    String message;
    ArrayList<AddressDistrictData> data;

    public AddressDistrictRep(int districtID, String message, ArrayList<AddressDistrictData> data) {
        DistrictID = districtID;
        this.message = message;
        this.data = data;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AddressDistrictData> getData() {
        return data;
    }

    public void setData(ArrayList<AddressDistrictData> data) {
        this.data = data;
    }
}
