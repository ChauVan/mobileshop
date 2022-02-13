package com.example.mobileshop.Object.Login;

public class LoginMessage {
    private int status;
    private String notification,fullName,token;

    public LoginMessage(int status, String notification, String fullName, String token) {
        this.status = status;
        this.notification = notification;
        this.fullName = fullName;
        this.token = token;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
