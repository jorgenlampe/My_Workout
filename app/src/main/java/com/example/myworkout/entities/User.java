package com.example.myworkout.entities;

import java.io.Serializable;

public class User implements Serializable {

    private String firebaseId;
    private String phone;
    private String email;
    private String name;
    private int birthYear;


    public User(String firebaseId, String phone, String email, String name, int birthYear) {
        this.firebaseId = firebaseId;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getFirebase_id() {
        return firebaseId;
    }

    public void setFirebase_id(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth_year() {
        return birthYear;
    }

    public void setBirth_year(int birthYear) {
        this.birthYear = birthYear;
    }
}
