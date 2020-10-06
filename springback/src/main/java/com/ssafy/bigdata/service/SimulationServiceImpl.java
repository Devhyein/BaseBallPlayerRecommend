package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dao.simulation.SimulationDao;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;

import org.springframework.beans.factory.annotation.Autowired;

public class SimulationServiceImpl implements SimulationService {

    @Autowired
    SimulationDao simulationDao;
    PlayerDao playerDao;

    @Override
    public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, int is_attack, int innings,
            boolean is_top, int out_count, String base_info, int my_score, int your_score, int hit_order) {
        return 0;
    }

    @Override
    public Simulation searchSimulation(int simulation_id) {
        return null;
    }

    @Override
    public int searchSimulationByUserId(int user_id) {
        return 0;
    }

    @Override
    public Simulation progressSimulation(Simulation simulation, int simulation_id, Score score, HitInfo hit_info,
            List<Integer> my_lineup, List<Integer> your_lineup) {

        StringBuilder sb = new StringBuilder();
        String[] base_info = simulation.getBase_info().split(",");
        int my_hit_order = simulation.getMy_hit_order();
        int your_hit_order = simulation.getYour_hit_order();
        int my_score = simulation.getMy_score();
        int out_count = simulation.getOut_count();
        int your_score = simulation.getYour_score();
        int strike = 0;
        int ball = 0;
        int foul = 0;
        int hitter_no = 0;
        boolean is_attack = simulation.isIs_attack();

        // 공격인 경우
        if (is_attack) {
            hitter_no = my_lineup.get(my_hit_order);
            RecordHitter hitter = playerDao.getPlayerStatsHitter(my_lineup.get(my_hit_order));
            RecordPitcher pitcher = playerDao.getPlayerStatsPitcher(your_lineup.get(9));
        }
        // 수비인 경우
        else {
            hitter_no = my_lineup.get(your_hit_order);
            RecordHitter hitter = playerDao.getPlayerStatsHitter(your_lineup.get(your_hit_order));
            RecordPitcher pitcher = playerDao.getPlayerStatsPitcher(my_lineup.get(9));
        }

        while (ball != 4 && strike != 3) {
            sb.append(hitter_no + "번 타자 ");

        }
        if (strike == 3) {
            sb.append("삼진 아웃!");
        } else if (ball == 4) {
            sb.append("볼넷!");
        } else {
            sb.append("진루");
        }

        return null;
    }

    @Override
    public int endSimulation(int simulation_id) {
        return 0;
    }

    @Override
    public int createScore(int simulation_id) {
        return 0;
    }

    @Override
    public Score searchScore(int simulation_id) {
        return null;
    }

    @Override
    public int updateScore(Score score) {
        return 0;
    }

    @Override
    public int createHitInfo(int simulation_id) {
        return 0;
    }

    @Override
    public HitInfo searchHitInfo(int simulation_id) {
        return null;
    }

    @Override
    public int updateHitInfo(HitInfo hit_info) {
        return 0;
    }

    @Override
    public int createLineup(Lineup lineup) {
        return 0;
    }

    @Override
    public Lineup searchLineup(int simulation_id) {
        return null;
    }

    @Override
    public Lineup updateLineup(Lineup lineup) {
        return null;
    }

    @Override
    public Lineup readLineUp(int user_id, int lineup_id) {
        return null;
    }

}
