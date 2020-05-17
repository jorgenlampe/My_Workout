package com.example.myworkout.entities;

public class ProgramType {

    private String id;
    private String rid;
    private String api_key;
    private String name;
    private String description;
    private String icon;
    private String backColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApi_key() {
        return api_key;
    }

    public ProgramType(){}

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public ProgramType(String id, String rid, String api_key, String description, String icon, String backColor) {
        this.id = id;
        this.rid = rid;
        this.description = description;
        this.backColor = backColor;
        this.icon = icon;
        this.api_key = api_key;
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
