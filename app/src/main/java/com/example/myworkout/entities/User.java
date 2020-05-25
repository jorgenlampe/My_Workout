package com.example.myworkout.entities;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String firebaseId;
    private String phone;
    private String email;
    private String name;
    private int id;
    private int birth_year;
    private List<UserProgram> user_programs;

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public int getId() {
        return id;
    }


    public List<UserProgram> getUser_programs() {
        return user_programs;
    }


    public User(int id, String firebaseId, String email, String phone, String name, int birth_year, List<UserProgram> user_programs) {
        this.firebaseId = firebaseId;
        this.phone = phone;
        this.email = email;
        this.user_programs = user_programs;
        this.name = name;
        this.birth_year = birth_year;
        this.id = id;
    }

    public int getUser_id() {
        return id;
    }


    public String getPhone() {
        return phone;
    }


    public String getEmail() {
        return email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth_year() {
        return 2020 - (birth_year/365/24/60);
    }


}