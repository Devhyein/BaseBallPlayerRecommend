package com.ssafy.bigdata.dto.simulation;

public class SimulationStart {
    private int user_id;
    private int my_lineup_id;
    private int your_lineup_id;
    private boolean is_attack;

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

    public SimulationStart() {
    }

    public SimulationStart(int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack) {
        this.user_id = user_id;
        this.my_lineup_id = my_lineup_id;
        this.your_lineup_id = your_lineup_id;
        this.is_attack = is_attack;
    }

    @Override
    public String toString() {
        return "SimulationStart [is_attack=" + is_attack + ", my_lineup_id=" + my_lineup_id + ", user_id=" + user_id
                + ", your_lineup_id=" + your_lineup_id + "]";
    }

    
}
