package com.example.mobileshop.Object.Login;

public class LoginSocial {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String user,fullName,email;

    public LoginSocial(String user, String fullName, String email) {
        this.user = user;
        this.fullName = fullName;
        this.email = email;
    }
}
