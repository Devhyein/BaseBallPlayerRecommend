package com.ssafy.bigdata.dto;

public class ToolsPitcher {
    private float era;
    private float health;
    private float controll;
    private float stability;
    private float deterrent;

    public ToolsPitcher() {
    }

    public ToolsPitcher(float era, float health, float controll, float stability, float deterrent) {
        this.era = era;
        this.health = health;
        this.controll = controll;
        this.stability = stability;
        this.deterrent = deterrent;
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

    public float getControll() {
        return controll;
    }

    public void setControll(float controll) {
        this.controll = controll;
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
        return "ToolsPitcher [controll=" + controll + ", deterrent=" + deterrent + ", era=" + era + ", health=" + health
                + ", stability=" + stability + "]";
    }

   
}
