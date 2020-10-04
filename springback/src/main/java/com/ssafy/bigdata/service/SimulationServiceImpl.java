package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dao.simulation.SimulationDao;
import com.ssafy.bigdata.dto.HitInfo;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.Score;
import com.ssafy.bigdata.dto.Simulation;

import org.springframework.beans.factory.annotation.Autowired;

public class SimulationServiceImpl implements SimulationService {

    @Autowired
    SimulationDao simulationDao;

    // 시물레이션 생성
    @Override
    public Simulation createSimulation() {
        int simulation_id = simulationDao.createSimulation();
        Simulation game = progressSimulation(simulation_id);
        return game;
    }

    // 시물레이션 조회
    @Override
    public Simulation searchSimulation(int simulation_id) {
        Simulation ret = simulationDao.searchSimulation(simulation_id);
        return ret;
    }

    // 시물레이션 진행
    @Override
    public Simulation progressSimulation(int simulation_id) {
        Simulation game = searchSimulation(simulation_id);
        // 

        return game;
    }

    // 시물레이션 종료
    @Override
    public int endSimulation(int simulation_id) {
        int ret = simulationDao.endSimulation(simulation_id);
        return ret;
    }

    // 점수 생성
    @Override
    public int createScore() {
        int ret = simulationDao.createScore();
        return ret;
    }

    // 점수 조회
    @Override
    public Score searchScore(int score_id) {
        Score ret = simulationDao.searchScore(score_id);
        return ret;
    }

    // 점수 수정
    @Override
    public int updateScore(Score score) {
        int ret = simulationDao.updateScore(score);
        return ret;
    }

    // 타석정보 생성
    @Override
    public int createHitInfo() {
        int ret = simulationDao.createHitInfo();
        return ret;
    }

    // 타석정보 조회
    @Override
    public HitInfo searchHitInfo(int hit_info_id) {
        HitInfo ret = simulationDao.searchHitInfo(hit_info_id);
        return ret;
    }

    // 타석정보 수정
    @Override
    public int updateHitInfo(HitInfo hit_info) {
        int ret = simulationDao.updateHitInfo(hit_info);
        return ret;
    }

    // 경기중 라인업 생성

    // 라인업 조회
    @Override
    public Lineup readLineUp() {
        return null;
    }

}
