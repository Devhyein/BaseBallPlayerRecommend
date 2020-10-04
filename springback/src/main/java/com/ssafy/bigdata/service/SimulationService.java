package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.SimulationGame;
import com.ssafy.bigdata.dto.hit_info;

public interface SimulationService {

    // 시뮬레이션 만들기
    public int createGame();

    // 게임 진행
    public SimulationGame progressGame();

    // 게임 종료
    public int endGame();

    // 게임 점수 조회
    public int[] readScore();

    // 타석 정보 조회
    public hit_info hitInfo();

    // 현재 나의 라인업 정보 조회
    public Lineup readLineUp();

}
