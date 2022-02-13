package com.example.mobileshop.Object.Person;

public class ChangePassword {
    String user,passOld,passNew;

    public ChangePassword(String user, String passOld, String passNew) {
        this.user = user;
        this.passOld = passOld;
        this.passNew = passNew;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassOld() {
        return passOld;
    }

    public void setPassOld(String passOld) {
        this.passOld = passOld;
    }

    public String getPassNew() {
        return passNew;
    }

    public void setPassNew(String passNew) {
        this.passNew = passNew;
    }
}
