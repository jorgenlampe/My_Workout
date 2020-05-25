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

}
