package com.ssafy.bigdata.model.dto;

public class Stadium {

    private int stadium_id;
    private String stadium_name;

    public Stadium() {
    }

    public Stadium(int stadium_id, String stadium_name) {
        this.stadium_id = stadium_id;
        this.stadium_name = stadium_name;
    }

    public int getStadium_id() {
        return stadium_id;
    }

    public void setStadium_id(int stadium_id) {
        this.stadium_id = stadium_id;
    }

    public String getStadium_name() {
        return stadium_name;
    }

    public void setStadium_name(String stadium_name) {
        this.stadium_name = stadium_name;
    }

    @Override
    public String toString() {
        return "Stadium [stadium_id=" + stadium_id + ", stadium_name=" + stadium_name + "]";
    }

}