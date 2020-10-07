package com.ssafy.bigdata.dao.simulation;

import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SimulationDao {

    // 시뮬레이션 생성
    public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack, int innings, boolean is_top, int out_count, String base_info, int my_score, int your_score, int my_hit_order, int your_hit_order, int game_status);

    // 시뮬레이션 조회
    public Simulation searchSimulation(int simulation_id);

    // 유저 아이디로 시뮬레이션 아이디 조회 (제일 최근꺼 1개)
    public int searchSimulationByUserId(int user_id);

    // 시뮬레이션 수정 ( 게임 진행시 )
    public int updateSimulation(Simulation simulation);

    // 시뮬레이션 제거
    public int deleteSimulation(int simulation_id);

    // 시뮬레이션 종료
    public int endSimulation(int simulation_id);

    // 스코어 생성
    public int createScore(int simulation_id);

    // 스코어 조회
    public Score searchScore(int simulation_id);

    // 스코어 수정 ( 게임 진행시 )
    public int updateScore(Score score);

    // 스코어 제거
    // public int deleteScore(int score_id);

    // 타석정보 생성
    public int createHitInfo(int simulation_id, int player_id);

    // 타석정보 조회
    public HitInfo searchHitInfo(int simulation_id, int player_id);

    // 타석정보 수정 ( 게임 진행시 )
    public int updateHitInfo(HitInfo hit_info);

    // 타석정보 제거
    // public int deleteHitInfo(int hit_info_id);

}
