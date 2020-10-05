package com.ssafy.bigdata.dto;

public class Favorites {
    private int favorites_id;
    private int user_id;
    private int player_id;

    public int getFavorites_id() {
        return favorites_id;
    }

    public void setFavorites_id(int favorites_id) {
        this.favorites_id = favorites_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public Favorites() {
    }

    public Favorites(int favorites_id, int user_id, int player_id) {
        this.favorites_id = favorites_id;
        this.user_id = user_id;
        this.player_id = player_id;
    }

    @Override
    public String toString() {
        return "Favorites [favorites_id=" + favorites_id + ", player_id=" + player_id + ", user_id=" + user_id + "]";
    }

    
}
