package com.example.mobileshop.Object.Address;

public class Address {
    int addressId;
    String user;
    String fullname,phone,city,district,wards,address;
    Boolean addressDefault,status;

    public Address(int addressId, String user, String fullname, String phone, String city, String district, String wards, String address, Boolean addressDefault, Boolean status) {
        this.addressId = addressId;
        this.user = user;
        this.fullname = fullname;
        this.phone = phone;
        this.city = city;
        this.district = district;
        this.wards = wards;
        this.address = address;
        this.addressDefault = addressDefault;
        this.status = status;
    }

    public Address(){

    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(Boolean addressDefault) {
        this.addressDefault = addressDefault;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
