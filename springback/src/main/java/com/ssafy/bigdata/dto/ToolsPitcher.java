package com.ssafy.bigdata.dto;

public class ToolsPitcher {
    private float era;
    private float health;
    private float control;
    private float stability;
    private float deterrent;

    public ToolsPitcher() {
    }

    public ToolsPitcher(float era, float health, float control, float stability, float deterrent) {
        this.era = era;
        this.health = health;
        this.control = control;
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
        return "ToolsPitcher [control=" + control + ", deterrent=" + deterrent + ", era=" + era + ", health=" + health
                + ", stability=" + stability + "]";
    }

   
}
