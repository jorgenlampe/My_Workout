package com.example.myworkout.entities;

import java.util.ArrayList;

public class UserProgramExercise {

    private String rid;
    private String user_program_id;
    private String app_exercise_id;

    public UserProgramExercise(String rid, String userProgramId, String exerciseId) {
        this.rid = rid;
        this.user_program_id = userProgramId;
        this.app_exercise_id = exerciseId;
    }

    public UserProgramExercise(){}

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUser_program_id() {
        return user_program_id;
    }

    public void setUser_program_id(String user_program_id) {
        this.user_program_id = user_program_id;
    }

    public String getApp_exercise_id() {
        return app_exercise_id;
    }

    public void setApp_exercise_id(String app_exercise_id) {
        this.app_exercise_id = app_exercise_id;
    }
}
