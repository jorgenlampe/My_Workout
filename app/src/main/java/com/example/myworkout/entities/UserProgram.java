package com.example.myworkout.entities;

import java.util.ArrayList;
import java.util.List;

public class UserProgram {

    private String name;
    private String description;
    private ProgramType programType;
    private List<Exercise> exercises;

    public UserProgram(String name, String description, ProgramType programType) {
        this.name = name;
        this.description = description;
        this.programType = programType;
        exercises = new ArrayList<>();
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

    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }
}
