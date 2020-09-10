package com.ssafy.bigdata.dto;

public class Player {
    private int player_id;
    private int team_id;
    private String player_name;
    private int player_num;
    private int player_age;
    private int player_position;

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getPlayer_num() {
        return player_num;
    }

    public void setPlayer_num(int player_num) {
        this.player_num = player_num;
    }

    public int getPlayer_age() {
        return player_age;
    }

    public void setPlayer_age(int player_age) {
        this.player_age = player_age;
    }

    public int getPlayer_position() {
        return player_position;
    }

    public void setPlayer_position(int player_position) {
        this.player_position = player_position;
    }

    @Override
    public String toString() {
        return "Player [player_age=" + player_age + ", player_id=" + player_id + ", player_name=" + player_name
                + ", player_num=" + player_num + ", player_position=" + player_position + ", team_id=" + team_id + "]";
    }

    public Player() {
    }

    public Player(int player_id, int team_id, String player_name, int player_num, int player_age, int player_position) {
        this.player_id = player_id;
        this.team_id = team_id;
        this.player_name = player_name;
        this.player_num = player_num;
        this.player_age = player_age;
        this.player_position = player_position;
    }

}