package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dto.Score;
import com.ssafy.bigdata.dto.HitInfo;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.Simulation;

public interface SimulationService {

    // 시뮬레이션 생성
    public Simulation createSimulation();

    // 시뮬레이션 조회
    public Simulation searchSimulation(int simulation_id);

    // 시뮬레이션 진행
    public Simulation progressSimulation(int simulation_id);

    // 시뮬레이션 종료
    public int endSimulation(int simulation_id);

    // 스코어 생성
    public int createScore();

    // 스코어 조회
    public Score searchScore(int score_id);

    // 스코어 수정
    public int updateScore(Score score);

    // 타석정보 생성
    public int createHitInfo();

    // 타석정보 조회
    public HitInfo searchHitInfo(int hit_info_id);

    // 타석정보 수정
    public int updateHitInfo(HitInfo hit_info);

    // 경기중 라인업 생성

    // 경기중 라인업 조회

    // 경기중 라인업 수정

    // 현재 나의 라인업 정보 조회  
    public Lineup readLineUp();

}
