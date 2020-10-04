package com.ssafy.bigdata.dto;

public class score {
    private int hit_info_id;
    private int simulation_id;
    private int player_id;
    private int at_bat_count;
    private int hit_count;
    private int homerun_count;
    private int foul_count;

    public score() {

    }

    public score(int hit_info_id, int simulation_id, int player_id, int at_bat_count, int hit_count, int homerun_count,
            int foul_count) {
        this.hit_info_id = hit_info_id;
        this.simulation_id = simulation_id;
        this.player_id = player_id;
        this.at_bat_count = at_bat_count;
        this.hit_count = hit_count;
        this.homerun_count = homerun_count;
        this.foul_count = foul_count;
    }

    public int getHit_info_id() {
        return hit_info_id;
    }

    public void setHit_info_id(int hit_info_id) {
        this.hit_info_id = hit_info_id;
    }

    public int getSimulation_id() {
        return simulation_id;
    }

    public void setSimulation_id(int simulation_id) {
        this.simulation_id = simulation_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getAt_bat_count() {
        return at_bat_count;
    }

    public void setAt_bat_count(int at_bat_count) {
        this.at_bat_count = at_bat_count;
    }

    public int getHit_count() {
        return hit_count;
    }

    public void setHit_count(int hit_count) {
        this.hit_count = hit_count;
    }

    public int getHomerun_count() {
        return homerun_count;
    }

    public void setHomerun_count(int homerun_count) {
        this.homerun_count = homerun_count;
    }

    public int getFoul_count() {
        return foul_count;
    }

    public void setFoul_count(int foul_count) {
        this.foul_count = foul_count;
    }

    @Override
    public String toString() {
        return "score [at_bat_count=" + at_bat_count + ", foul_count=" + foul_count + ", hit_count=" + hit_count
                + ", hit_info_id=" + hit_info_id + ", homerun_count=" + homerun_count + ", player_id=" + player_id
                + ", simulation_id=" + simulation_id + "]";
    }

}
