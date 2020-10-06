package com.ssafy.bigdata.dto.simulation;

public class SimulationData {
    Simulation game;
    Score score;
    HitInfo hit_info;

    public SimulationData() {
    }

    public SimulationData(Simulation game, Score score, HitInfo hit_info) {
        this.game = game;
        this.score = score;
        this.hit_info = hit_info;
    }

    public Simulation getGame() {
        return game;
    }

    public void setGame(Simulation game) {
        this.game = game;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public HitInfo getHit_info() {
        return hit_info;
    }

    public void setHit_info(HitInfo hit_info) {
        this.hit_info = hit_info;
    }

    @Override
    public String toString() {
        return "SimulationData [game=" + game + ", hit_info=" + hit_info + ", my_lineup=" + score + "]";
    }

}