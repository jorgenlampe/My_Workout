package com.example.myworkout.entities;

public class UserProgramSession {

    private String user_program_id;
    private String date;
    private Float time_spent;
    private String description;
    private String extra_json_data;
    private String id;
    private String rid;

    public String getId() {
        return id;
    }

    public String getRid() {
        return rid;
    }

    public UserProgramSession(String id, String rid, String user_program_id, String date, Float timeSpent, String description, String extraJsonData) {
        this.id = id;
        this.rid = rid;
        this.user_program_id = user_program_id;
        this.date = date;
        this.time_spent = timeSpent;
        this.description = description;
        this.extra_json_data = extraJsonData;
    }


    public String getUserProgramId() {
        return user_program_id;
    }

    public String getDate() {
        return date;
    }

    public Float getTime_spent() {
        return time_spent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra_json_data() {
        return extra_json_data;
    }

}
