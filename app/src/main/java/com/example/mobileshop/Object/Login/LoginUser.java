package com.example.mobileshop.Object.Login;

public class LoginUser {
    private String User,Pass;

    public LoginUser(String user, String pass) {
        User = user;
        Pass = pass;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
