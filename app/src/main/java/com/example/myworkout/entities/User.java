package com.example.myworkout.entities;

import java.io.Serializable;

public class User implements Serializable {

    private String firebaseId;
    private String phone;
    private String email;
    private String name;
    private int birth_year;


    //TODO User-objektet inneholder igjen objekter av andre typer (UserProgram, UserProgramExercises osv.)

    public User(String firebaseId, String phone, String email, String name, int birth_year) {
        this.firebaseId = firebaseId;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.birth_year = birth_year;
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
        return birth_year;
    }

    public void setBirth_year(int birthYear) {
        this.birth_year = birthYear;
    }
}