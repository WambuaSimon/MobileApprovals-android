package com.wizag.mobileapprovals.models;

public class People {

    private String name;
    private String id;

    public People(String name ,String id) {
        this.name = name;

        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}