package com.example.mobileshop.Object.Address.AddressOutAPI;

public class AddressProvinceData {
    int ProvinceID,CountryID;
    String ProvinceName;

    public AddressProvinceData(int provinceID, int countryID, String provinceName) {
        ProvinceID = provinceID;
        CountryID = countryID;
        ProvinceName = provinceName;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int provinceID) {
        ProvinceID = provinceID;
    }

    public int getCountryID() {
        return CountryID;
    }

    public void setCountryID(int countryID) {
        CountryID = countryID;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }
}
