package com.ssafy.bigdata.model.dto;

public class Team {
    private int team_id;
    private int stadium_id;
    private String team_name;

    public Team() {
    }

    public Team(int team_id, int stadium_id, String team_name) {
        this.team_id = team_id;
        this.stadium_id = stadium_id;
        this.team_name = team_name;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getStadium_id() {
        return stadium_id;
    }

    public void setStadium_id(int stadium_id) {
        this.stadium_id = stadium_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    @Override
    public String toString() {
        return "Team [stadium_id=" + stadium_id + ", team_id=" + team_id + ", team_name=" + team_name + "]";
    }

}