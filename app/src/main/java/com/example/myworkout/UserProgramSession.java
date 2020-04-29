package com.example.myworkout;

import java.util.Date;

class UserProgramSession {

    private String userProgramId;
    private Date date;
    private long timeSpent;
    private String description;
    private String extraJsonData;

    public UserProgramSession(String userProgramId, Date date, long timeSpent, String description, String extraJsonData) {
        this.userProgramId = userProgramId;
        this.date = date;
        this.timeSpent = timeSpent;
        this.description = description;
        this.extraJsonData = extraJsonData;
    }

    public String getUserProgramId() {
        return userProgramId;
    }

    public void setUserProgramId(String userProgramId) {
        this.userProgramId = userProgramId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraJsonData() {
        return extraJsonData;
    }

    public void setExtraJsonData(String extraJsonData) {
        this.extraJsonData = extraJsonData;
    }
}
