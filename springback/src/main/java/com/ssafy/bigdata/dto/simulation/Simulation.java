package com.ssafy.bigdata.dto.simulation;

public class Simulation {

    private int simulation_id;
    private int user_id;
    private int my_lineup_id;
    private int your_lineup_id;
    private boolean is_attack;
    private int innings;
    private boolean is_top;
    private int out_count;
    private String base_info;
    private int[] base_info_array;
    private int my_score;
    private int your_score;
    private int my_hit_order;
    private int your_hit_order;
    private boolean game_status;

    public Simulation() {

    }

    public Simulation(int simulation_id, int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack,
            int innings, boolean is_top, int out_count, String base_info, int my_score, int your_score,
            int my_hit_order, int your_hit_order, boolean game_status) {
        this.simulation_id = simulation_id;
        this.user_id = user_id;
        this.my_lineup_id = my_lineup_id;
        this.your_lineup_id = your_lineup_id;
        this.is_attack = is_attack;
        this.innings = innings;
        this.is_top = is_top;
        this.out_count = out_count;
        this.base_info = base_info;
        this.my_score = my_score;
        this.your_score = your_score;
        this.my_hit_order = my_hit_order;
        this.your_hit_order = your_hit_order;
        this.game_status = game_status;
    }

    public Simulation(int simulation_id, int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack,
            int innings, boolean is_top, int out_count, int[] base_info_array, int my_score, int your_score,
            int my_hit_order, int your_hit_order, boolean game_status) {
        this.simulation_id = simulation_id;
        this.user_id = user_id;
        this.my_lineup_id = my_lineup_id;
        this.your_lineup_id = your_lineup_id;
        this.is_attack = is_attack;
        this.innings = innings;
        this.is_top = is_top;
        this.out_count = out_count;
        this.base_info_array = base_info_array;
        this.my_score = my_score;
        this.your_score = your_score;
        this.my_hit_order = my_hit_order;
        this.your_hit_order = your_hit_order;
        this.game_status = game_status;
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

    public int getMy_hit_order() {
        return my_hit_order;
    }

    public void setMy_hit_order(int my_hit_order) {
        this.my_hit_order = my_hit_order;
    }

    public int getYour_hit_order() {
        return your_hit_order;
    }

    public void setYour_hit_order(int your_hit_order) {
        this.your_hit_order = your_hit_order;
    }

    public boolean isGame_status() {
        return game_status;
    }

    public void setGame_status(boolean game_status) {
        this.game_status = game_status;
    }

}