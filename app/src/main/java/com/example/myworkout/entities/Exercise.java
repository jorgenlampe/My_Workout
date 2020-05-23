package com.example.myworkout.entities;

public class Exercise {

    private String id;
    private String rid;
    private String name;
    private String description;
    private String icon;
    private String infobox_color;
    private String user_program_id;
   // private String programName;


    public String getUser_program_id() {
        return user_program_id;
    }

    public Exercise(String id, String rid, String name, String description, String icon, String infobox_color) {
        this.id = id;
        this.rid = rid;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.infobox_color = infobox_color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setInfobox_color(String infobox_color) {
        this.infobox_color = infobox_color;
    }

    public String getInfobox_color() {
        return infobox_color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }


}
