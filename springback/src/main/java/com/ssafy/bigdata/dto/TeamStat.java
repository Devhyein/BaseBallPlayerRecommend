package com.ssafy.bigdata.dto;

public class TeamStat {
    private int team_id;
    private float power;
    private float speed;
    private float contact;
    private float defense;
    private float shoulder;
    private float era;
    private float health;
    private float control;
    private float stability;
    private float deterrent;

    public TeamStat() {
    }

    public TeamStat(int team_id, float power, float speed, float contact, float defense, float shoulder, float era, float health,
            float control, float stability, float deterrent) {
        this.team_id = team_id;
        this.power = power;
        this.speed = speed;
        this.contact = contact;
        this.defense = defense;
        this.shoulder = shoulder;
        this.era = era;
        this.health = health;
        this.control = control;
        this.stability = stability;
        this.deterrent = deterrent;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getContact() {
        return contact;
    }

    public void setContact(float contact) {
        this.contact = contact;
    }

    public float getDefense() {
        return defense;
    }

    public void setDefense(float defense) {
        this.defense = defense;
    }

    public float getShoulder() {
        return shoulder;
    }

    public void setShoulder(float shoulder) {
        this.shoulder = shoulder;
    }

    public float getEra() {
        return era;
    }

    public void setEra(float era) {
        this.era = era;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getControl() {
        return control;
    }

    public void setControl(float control) {
        this.control = control;
    }

    public float getStability() {
        return stability;
    }

    public void setStability(float stability) {
        this.stability = stability;
    }

    public float getDeterrent() {
        return deterrent;
    }

    public void setDeterrent(float deterrent) {
        this.deterrent = deterrent;
    }

    @Override
    public String toString() {
        return "TeamStat [contact=" + contact + ", control=" + control + ", defense=" + defense + ", deterrent="
                + deterrent + ", era=" + era + ", health=" + health + ", power=" + power + ", shoulder=" + shoulder
                + ", speed=" + speed + ", stability=" + stability + "]";
    }

    
}
