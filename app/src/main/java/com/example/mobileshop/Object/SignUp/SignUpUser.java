package com.example.mobileshop.Object.SignUp;

public class SignUpUser {
    String user,fullname,pass,phonenumber,email;

    public SignUpUser(String user, String fullname, String pass, String phonenumber, String email) {
        this.user = user;
        this.fullname = fullname;
        this.pass = pass;
        this.phonenumber = phonenumber;
        this.email = email;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
