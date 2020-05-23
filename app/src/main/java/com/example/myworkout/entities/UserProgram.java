package com.example.myworkout.entities;

import java.util.List;

public class UserProgram {

    private String name;
    private String description;
    private String rid;
    private String app_program_type_id;
    private int use_timing;
    private String user_id;
    private String id;
    private String api_key;
    private List<UserProgramExercise> user_program_exercises;
    private List<UserProgramSession> user_program_sessions;
    private ProgramType app_program_type;

    public UserProgram(String id, String rid, String api_key, String user_id, String app_program_type_id, String name, String description, int use_timing, List<UserProgramExercise> user_program_exercises,
                       List<UserProgramSession> user_program_sessions, ProgramType app_program_type){
        this.name = name;
        this.user_id = user_id;
        this.description = description;
        this.app_program_type_id = app_program_type_id;
        this.use_timing = use_timing;
        this.id = id;
        this.rid = rid;
        this.app_program_type = app_program_type;
        this.api_key = api_key;
        this.user_program_exercises = user_program_exercises;
        this.user_program_sessions = user_program_sessions;
    }

    public void setApp_program_type_id(String app_program_type_id) {
        this.app_program_type_id = app_program_type_id;
    }

    public void setUse_timing(int use_timing) {
        this.use_timing = use_timing;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public List<UserProgramExercise> getUser_program_exercises() {
        return user_program_exercises;
    }

    public void setUser_program_exercises(List<UserProgramExercise> user_program_exercises) {
        this.user_program_exercises = user_program_exercises;
    }

    public List<UserProgramSession> getUser_program_sessions() {
        return user_program_sessions;
    }

    public void setUser_program_sessions(List<UserProgramSession> user_program_sessions) {
        this.user_program_sessions = user_program_sessions;
    }

    public ProgramType getApp_program_type() {
        return app_program_type;
    }

    public void setApp_program_type(ProgramType app_program_type) {
        this.app_program_type = app_program_type;
    }

    public String getApp_program_type_id() {
        return app_program_type_id;
    }

    public int getUse_timing() {
        return use_timing;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString(){

        return getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


