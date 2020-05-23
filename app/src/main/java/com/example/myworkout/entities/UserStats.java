package com.example.myworkout.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserStats implements Serializable {

    private int id;
    private String firebase_id;
    private String api_key;
    private Map<String, Float> last7days;
    private Map<String, Float> currentWeek;
    private Map<String, Float> currentMonth;
    private Map<String, Float> last30days;
    private Map<String, Float> currentYear;

    public int getId() {
        return id;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public Map<String, Float> getLast7days() {
        return last7days;
    }

    public Map<String, Float> getCurrentWeek() {
        return currentWeek;
    }

    public Map<String, Float> getCurrentMonth() {
        return currentMonth;
    }

    public Map<String, Float> getLast30days() {
        return last30days;
    }

    public Map<String, Float> getCurrentYear() {
        return currentYear;
    }

    public UserStats(int id, String firebase_id, String api_key, Map<String, Float> last7days, Map<String, Float> currentWeek, Map<String, Float> currentMonth, Map<String, Float> last30days, Map<String, Float> currentYear) {
        this.id = id;
        this.firebase_id = firebase_id;
        this.api_key = api_key;
        this.last7days = last7days;
        this.currentWeek = currentWeek;
        this.currentMonth = currentMonth;
        this.last30days = last30days;
        this.currentYear = currentYear;
    }
}