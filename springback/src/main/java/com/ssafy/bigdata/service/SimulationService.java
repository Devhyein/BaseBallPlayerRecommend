package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;

public interface SimulationService {

    // 시뮬레이션 생성
    public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, int is_attack, int innings,
            boolean is_top, int out_count, String base_info, int my_score, int your_score, int hit_order);

    // 시뮬레이션 조회
    public Simulation searchSimulation(int simulation_id);

    // 유저아이디로 시뮬레이션 아이디 조회
    public int searchSimulationByUserId(int user_id);

    // 시뮬레이션 진행
    public Simulation progressSimulation(Simulation simulation,int simulation_id, Score score, HitInfo hit_info, List<Integer> my_lineup, List<Integer> your_lineup);

    // 시뮬레이션 종료
    public int endSimulation(int simulation_id);

    // 스코어 생성
    public int createScore(int simulation_id);

    // 스코어 조회
    public Score searchScore(int simulation_id);

    // 스코어 수정
    public int updateScore(Score score);

    // 타석정보 생성
    public int createHitInfo(int simulation_id);

    // 타석정보 조회
    public HitInfo searchHitInfo(int simulation_id);

    // 타석정보 수정
    public int updateHitInfo(HitInfo hit_info);

    // 경기중 라인업 생성
    public int createLineup(Lineup lineup);

    // 경기중 라인업 조회
    public Lineup searchLineup(int simulation_id);

    // 경기중 라인업 수정
    public Lineup updateLineup(Lineup lineup);

    // 현재 나의 라인업 정보 조회
    public Lineup readLineUp(int user_id, int lineup_id);

}
