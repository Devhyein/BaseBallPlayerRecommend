package com.ssafy.bigdata.dto.simulation;

import java.util.Arrays;

public class Score {
    private int score_id;
    private int simulation_id;
    private String my_score;
    private int[] my_score_array;
    private String your_score;
    private int[] your_score_array;

    Score() {
    }

    public Score(int score_id, int simulation_id, String my_score, String your_score) {
        this.score_id = score_id;
        this.simulation_id = simulation_id;
        this.my_score = my_score;
        this.your_score = your_score;
    }

    public Score(int score_id, int simulation_id, int[] my_score_array, int[] your_score_array) {
        this.score_id = score_id;
        this.simulation_id = simulation_id;
        this.my_score_array = my_score_array;
        this.your_score_array = your_score_array;
    }

    public int getScore_id() {
        return score_id;
    }

    public void setScore_id(int score_id) {
        this.score_id = score_id;
    }

    public int getSimulation_id() {
        return simulation_id;
    }

    public void setSimulation_id(int simulation_id) {
        this.simulation_id = simulation_id;
    }

    public String getMy_score() {
        return my_score;
    }

    public void setMy_score(String my_score) {
        this.my_score = my_score;
    }

    public int[] getMy_score_array() {
        return my_score_array;
    }

    public void setMy_score_array(int[] my_score_array) {
        this.my_score_array = my_score_array;
    }

    public String getYour_score() {
        return your_score;
    }

    public void setYour_score(String your_score) {
        this.your_score = your_score;
    }

    public int[] getYour_score_array() {
        return your_score_array;
    }

    public void setYour_score_array(int[] your_score_array) {
        this.your_score_array = your_score_array;
    }

    @Override
    public String toString() {
        return "hit_info [my_score=" + my_score + ", my_score_array=" + Arrays.toString(my_score_array) + ", score_id="
                + score_id + ", simulation_id=" + simulation_id + ", your_score=" + your_score + ", your_score_array="
                + Arrays.toString(your_score_array) + "]";
    }

}
