package com.example.myworkout.entities;

class UserProgramExercise {

    private String rid;
    private String userProgramId;
    private String exerciseId;

    public UserProgramExercise(String rid, String userProgramId, String exerciseId) {
        this.rid = rid;
        this.userProgramId = userProgramId;
        this.exerciseId = exerciseId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUserProgramId() {
        return userProgramId;
    }

    public void setUserProgramId(String userProgramId) {
        this.userProgramId = userProgramId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }
}
