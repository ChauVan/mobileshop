package com.example.mobileshop.Object.Address.AddressOutAPI;

import java.util.ArrayList;

public class AddressDistrictData {
    int DistrictID,ProvinceID;
    String DistrictName;

    public AddressDistrictData(int districtID, int provinceID, String districtName) {
        DistrictID = districtID;
        ProvinceID = provinceID;
        DistrictName = districtName;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }
}
