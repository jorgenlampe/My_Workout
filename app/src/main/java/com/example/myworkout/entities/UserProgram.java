package com.example.myworkout.entities;

public class UserProgram {

    private String name;
    private String description;
    private int app_program_type_id;
    private int use_timing;
    private String user_id;
    private int id;

    public int getApp_program_type_id() {
        return app_program_type_id;
    }

    public int getUse_timing() {
        return use_timing;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public UserProgram(int id, String name, String description, int app_program_type_id, int use_timing, String user_id) {
        this.name = name;
        this.user_id = user_id;
        this.description = description;
        this.app_program_type_id = app_program_type_id;
        this.use_timing = use_timing;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


