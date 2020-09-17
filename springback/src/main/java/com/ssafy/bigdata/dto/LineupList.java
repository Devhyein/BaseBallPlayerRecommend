package com.ssafy.bigdata.dto;

public class LineupList {
    private int id;
    private String name;

    public LineupList() {
    }

    public LineupList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LineupList [id=" + id + ", name=" + name + "]";
    }

    
}
