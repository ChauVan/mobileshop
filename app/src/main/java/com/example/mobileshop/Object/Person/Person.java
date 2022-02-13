package com.example.mobileshop.Object.Person;

public class Person {
    String user;
    String fullName;
    String phoneNumber;
    String email;
    String date;
    Boolean sex;

    public Person(String user, String fullName, String phoneNumber, String email, String date, Boolean sex) {
        this.user = user;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.date = date;
        this.sex = sex;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
