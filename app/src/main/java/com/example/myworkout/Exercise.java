package com.example.myworkout;

public class Exercise {

    private String id;
    private String rid;
    private String name;
    private String description;
    private String icon;
    private String infobox_oolor;
    //private String program_id;
   // private String programName;




    public Exercise(String id, String rid, String name, String description, String icon, String infobox_oolor) {
        this.id = id;
        this.rid = rid;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.infobox_oolor = infobox_oolor;
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

    public void setInfobox_oolor(String infobox_oolor) {
        this.infobox_oolor = infobox_oolor;
    }

    public String getInfobox_oolor() {
        return infobox_oolor;
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
