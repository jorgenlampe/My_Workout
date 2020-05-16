package com.example.myworkout.entities;

public class ProgramType {

    private String id;
    private String rid;
    private String name;
    private String description;
    private String backColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProgramType(String id, String rid, String name, String description, String backColor) {
        this.id = id;
        this.rid = rid;
        this.name = name;
        this.description = description;
        this.backColor = backColor;
    }

    @Override
    public String toString(){
        return getDescription();
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
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
