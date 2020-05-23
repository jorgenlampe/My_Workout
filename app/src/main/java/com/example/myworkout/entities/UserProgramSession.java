package com.example.myworkout.entities;

public class UserProgramSession {

    private String userProgramId;
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

    public UserProgramSession(String id, String rid, String userProgramId, String date, Float timeSpent, String description, String extraJsonData) {
        this.id = id;
        this.rid = rid;
        this.userProgramId = userProgramId;
        this.date = date;
        this.time_spent = timeSpent;
        this.description = description;
        this.extra_json_data = extraJsonData;
    }


    public String getUserProgramId() {
        return userProgramId;
    }

    public void setUserProgramId(String userProgramId) {
        this.userProgramId = userProgramId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public void setExtra_json_data(String extra_json_data) {
        this.extra_json_data = extra_json_data;
    }
}
