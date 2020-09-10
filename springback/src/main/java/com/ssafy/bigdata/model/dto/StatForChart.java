package com.ssafy.bigdata.model.dto;

public class StatForChart {
    private String stat_name;
    private float stat_value;
    private float stat_mean;
    private float stat_std;

    public StatForChart() {
    }

    public StatForChart(String stat_name, float stat_value, float stat_mean, float stat_std) {
        this.stat_name = stat_name;
        this.stat_value = stat_value;
        this.stat_mean = stat_mean;
        this.stat_std = stat_std;
    }

    public String getStat_name() {
        return stat_name;
    }

    public void setStat_name(String stat_name) {
        this.stat_name = stat_name;
    }

    public float getStat_value() {
        return stat_value;
    }

    public void setStat_value(float stat_value) {
        this.stat_value = stat_value;
    }

    public float getStat_mean() {
        return stat_mean;
    }

    public void setStat_mean(float stat_mean) {
        this.stat_mean = stat_mean;
    }

    public float getStat_std() {
        return stat_std;
    }

    public void setStat_std(float stat_std) {
        this.stat_std = stat_std;
    }

    @Override
    public String toString() {
        return "StatForChart [stat_mean=" + stat_mean + ", stat_name=" + stat_name + ", stat_std=" + stat_std
                + ", stat_value=" + stat_value + "]";
    }

}
