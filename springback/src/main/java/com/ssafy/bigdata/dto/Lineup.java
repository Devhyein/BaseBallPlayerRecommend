package com.ssafy.bigdata.dto;

public class Lineup {
    private int lineup_id;
    private int team_id;
    private int hitter1;
    private int hitter2;
    private int hitter3;
    private int hitter4;
    private int hitter5;
    private int hitter6;
    private int hitter7;
    private int hitter8;
    private int hitter9;
    private int pitcher;

    public Lineup() {
    }

    public Lineup(int lineup_id, int team_id, int hitter1, int hitter2, int hitter3, int hitter4, int hitter5,
            int hitter6, int hitter7, int hitter8, int hitter9, int pitcher) {
        this.lineup_id = lineup_id;
        this.team_id = team_id;
        this.hitter1 = hitter1;
        this.hitter2 = hitter2;
        this.hitter3 = hitter3;
        this.hitter4 = hitter4;
        this.hitter5 = hitter5;
        this.hitter6 = hitter6;
        this.hitter7 = hitter7;
        this.hitter8 = hitter8;
        this.hitter9 = hitter9;
        this.pitcher = pitcher;
    }

    public int getLineup_id() {
        return lineup_id;
    }

    public void setLineup_id(int lineup_id) {
        this.lineup_id = lineup_id;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getHitter1() {
        return hitter1;
    }

    public void setHitter1(int hitter1) {
        this.hitter1 = hitter1;
    }

    public int getHitter2() {
        return hitter2;
    }

    public void setHitter2(int hitter2) {
        this.hitter2 = hitter2;
    }

    public int getHitter3() {
        return hitter3;
    }

    public void setHitter3(int hitter3) {
        this.hitter3 = hitter3;
    }

    public int getHitter4() {
        return hitter4;
    }

    public void setHitter4(int hitter4) {
        this.hitter4 = hitter4;
    }

    public int getHitter5() {
        return hitter5;
    }

    public void setHitter5(int hitter5) {
        this.hitter5 = hitter5;
    }

    public int getHitter6() {
        return hitter6;
    }

    public void setHitter6(int hitter6) {
        this.hitter6 = hitter6;
    }

    public int getHitter7() {
        return hitter7;
    }

    public void setHitter7(int hitter7) {
        this.hitter7 = hitter7;
    }

    public int getHitter8() {
        return hitter8;
    }

    public void setHitter8(int hitter8) {
        this.hitter8 = hitter8;
    }

    public int getHitter9() {
        return hitter9;
    }

    public void setHitter9(int hitter9) {
        this.hitter9 = hitter9;
    }

    public int getPitcher() {
        return pitcher;
    }

    public void setPitcher(int pitcher) {
        this.pitcher = pitcher;
    }

    @Override
    public String toString() {
        return "Lineup [hitter1=" + hitter1 + ", hitter2=" + hitter2 + ", hitter3=" + hitter3 + ", hitter4=" + hitter4
                + ", hitter5=" + hitter5 + ", hitter6=" + hitter6 + ", hitter7=" + hitter7 + ", hitter8=" + hitter8
                + ", hitter9=" + hitter9 + ", lineup_id=" + lineup_id + ", pitcher=" + pitcher + ", team_id=" + team_id
                + "]";
    }

    
    
}
