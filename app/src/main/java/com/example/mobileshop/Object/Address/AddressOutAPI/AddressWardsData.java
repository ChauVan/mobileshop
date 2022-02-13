package com.example.mobileshop.Object.Address.AddressOutAPI;

public class AddressWardsData {
    int DistrictID;
    String WardName;

    public AddressWardsData(int districtID, String wardName) {
        DistrictID = districtID;
        WardName = wardName;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getWardName() {
        return WardName;
    }

    public void setWardName(String wardName) {
        WardName = wardName;
    }
}
