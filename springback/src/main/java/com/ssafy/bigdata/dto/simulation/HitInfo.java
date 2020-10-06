package com.ssafy.bigdata.dto.simulation;

public class HitInfo {
    private int hit_info_id;
    private int simulation_id;
    private int player_id;
    private int at_bat_count;
    private int homerun_count;
    private int hit1_count;
    private int hit2_count;
    private int hit3_count;
    private int foul_count;

    public HitInfo() {

    }

    public HitInfo(int hit_info_id, int simulation_id, int player_id, int at_bat_count, int homerun_count,
            int hit1_count, int hit2_count, int hit3_count, int foul_count) {
        this.hit_info_id = hit_info_id;
        this.simulation_id = simulation_id;
        this.player_id = player_id;
        this.at_bat_count = at_bat_count;
        this.homerun_count = homerun_count;
        this.hit1_count = hit1_count;
        this.hit2_count = hit2_count;
        this.hit3_count = hit3_count;
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

    public int getHomerun_count() {
        return homerun_count;
    }

    public void setHomerun_count(int homerun_count) {
        this.homerun_count = homerun_count;
    }

    public int getHit1_count() {
        return hit1_count;
    }

    public void setHit1_count(int hit1_count) {
        this.hit1_count = hit1_count;
    }

    public int getHit2_count() {
        return hit2_count;
    }

    public void setHit2_count(int hit2_count) {
        this.hit2_count = hit2_count;
    }

    public int getHit3_count() {
        return hit3_count;
    }

    public void setHit3_count(int hit3_count) {
        this.hit3_count = hit3_count;
    }

    public int getFoul_count() {
        return foul_count;
    }

    public void setFoul_count(int foul_count) {
        this.foul_count = foul_count;
    }

    @Override
    public String toString() {
        return "HitInfo [at_bat_count=" + at_bat_count + ", foul_count=" + foul_count + ", hit1_count=" + hit1_count
                + ", hit2_count=" + hit2_count + ", hit3_count=" + hit3_count + ", hit_info_id=" + hit_info_id
                + ", homerun_count=" + homerun_count + ", player_id=" + player_id + ", simulation_id=" + simulation_id
                + "]";
    }

}
