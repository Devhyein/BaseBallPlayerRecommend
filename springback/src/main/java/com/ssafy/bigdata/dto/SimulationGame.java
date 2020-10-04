package com.ssafy.bigdata.dto;

import java.util.Arrays;

public class SimulationGame {

    private int simulation_id;
    private int user_id;
    private int my_lineup_id;
    private int your_lineup_id;
    private String my_lineup_play;
    private String[] my_lineup_play_array;
    private boolean is_attack;
    private int innings;
    private boolean is_top;
    private int out_count;
    private String base_info;
    private int[] base_info_array;
    private int my_score;
    private int your_score;
    private int hit_order;
    private int replaced_player;
    private int[] replaced_player_array;

    public SimulationGame() {

    }

    public SimulationGame(int simulation_id, int user_id, int my_lineup_id, int your_lineup_id, String my_lineup_play,
            boolean is_attack, int innings, boolean is_top, int out_count, String base_info, int my_score,
            int your_score, int hit_order, int replaced_player) {
        this.simulation_id = simulation_id;
        this.user_id = user_id;
        this.my_lineup_id = my_lineup_id;
        this.your_lineup_id = your_lineup_id;
        this.my_lineup_play = my_lineup_play;
        this.is_attack = is_attack;
        this.innings = innings;
        this.is_top = is_top;
        this.out_count = out_count;
        this.base_info = base_info;
        this.my_score = my_score;
        this.your_score = your_score;
        this.hit_order = hit_order;
        this.replaced_player = replaced_player;
    }

    public SimulationGame(int simulation_id, int user_id, int my_lineup_id, int your_lineup_id,
            String[] my_lineup_play_array, boolean is_attack, int innings, boolean is_top, int out_count,
            int[] base_info_array, int my_score, int your_score, int hit_order, int[] replaced_player_array) {
        this.simulation_id = simulation_id;
        this.user_id = user_id;
        this.my_lineup_id = my_lineup_id;
        this.your_lineup_id = your_lineup_id;
        this.my_lineup_play_array = my_lineup_play_array;
        this.is_attack = is_attack;
        this.innings = innings;
        this.is_top = is_top;
        this.out_count = out_count;
        this.base_info_array = base_info_array;
        this.my_score = my_score;
        this.your_score = your_score;
        this.hit_order = hit_order;
        this.replaced_player_array = replaced_player_array;
    }

    public int getSimulation_id() {
        return simulation_id;
    }

    public void setSimulation_id(int simulation_id) {
        this.simulation_id = simulation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMy_lineup_id() {
        return my_lineup_id;
    }

    public void setMy_lineup_id(int my_lineup_id) {
        this.my_lineup_id = my_lineup_id;
    }

    public int getYour_lineup_id() {
        return your_lineup_id;
    }

    public void setYour_lineup_id(int your_lineup_id) {
        this.your_lineup_id = your_lineup_id;
    }

    public String getMy_lineup_play() {
        return my_lineup_play;
    }

    public void setMy_lineup_play(String my_lineup_play) {
        this.my_lineup_play = my_lineup_play;
    }

    public String[] getMy_lineup_play_array() {
        return my_lineup_play_array;
    }

    public void setMy_lineup_play_array(String[] my_lineup_play_array) {
        this.my_lineup_play_array = my_lineup_play_array;
    }

    public boolean isIs_attack() {
        return is_attack;
    }

    public void setIs_attack(boolean is_attack) {
        this.is_attack = is_attack;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public boolean isIs_top() {
        return is_top;
    }

    public void setIs_top(boolean is_top) {
        this.is_top = is_top;
    }

    public int getOut_count() {
        return out_count;
    }

    public void setOut_count(int out_count) {
        this.out_count = out_count;
    }

    public String getBase_info() {
        return base_info;
    }

    public void setBase_info(String base_info) {
        this.base_info = base_info;
    }

    public int[] getBase_info_array() {
        return base_info_array;
    }

    public void setBase_info_array(int[] base_info_array) {
        this.base_info_array = base_info_array;
    }

    public int getMy_score() {
        return my_score;
    }

    public void setMy_score(int my_score) {
        this.my_score = my_score;
    }

    public int getYour_score() {
        return your_score;
    }

    public void setYour_score(int your_score) {
        this.your_score = your_score;
    }

    public int getHit_order() {
        return hit_order;
    }

    public void setHit_order(int hit_order) {
        this.hit_order = hit_order;
    }

    public int getReplaced_player() {
        return replaced_player;
    }

    public void setReplaced_player(int replaced_player) {
        this.replaced_player = replaced_player;
    }

    public int[] getReplaced_player_array() {
        return replaced_player_array;
    }

    public void setReplaced_player_array(int[] replaced_player_array) {
        this.replaced_player_array = replaced_player_array;
    }

    @Override
    public String toString() {
        return "SimulationGame [base_info=" + base_info + ", base_info_array=" + Arrays.toString(base_info_array)
                + ", hit_order=" + hit_order + ", innings=" + innings + ", is_attack=" + is_attack + ", is_top="
                + is_top + ", my_lineup_id=" + my_lineup_id + ", my_lineup_play=" + my_lineup_play
                + ", my_lineup_play_array=" + Arrays.toString(my_lineup_play_array) + ", my_score=" + my_score
                + ", out_count=" + out_count + ", replaced_player=" + replaced_player + ", replaced_player_array="
                + Arrays.toString(replaced_player_array) + ", simulation_id=" + simulation_id + ", user_id=" + user_id
                + ", your_lineup_id=" + your_lineup_id + ", your_score=" + your_score + "]";
    }

}