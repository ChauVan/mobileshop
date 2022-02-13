package com.example.mobileshop.Object.Person;

public class InfoPerson {
    int status;
    String notification;
    Person data ;

    public InfoPerson(int status, String notification, Person data) {
        this.status = status;
        this.notification = notification;
        this.data = data;
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

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }
}
