package com.ssafy.bigdata.dao.simulation;

import com.ssafy.bigdata.dto.HitInfo;
import com.ssafy.bigdata.dto.Score;
import com.ssafy.bigdata.dto.Simulation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SimulationDao {

    // 시뮬레이션 생성
    public int createSimulation();

    // 시뮬레이션 조회
    public Simulation searchSimulation(int simulation_id);

    // 시뮬레이션 수정 ( 게임 진행시 )
    public int updateSimulation(Simulation simulation);

    // 시뮬레이션 제거
    public int deleteSimulation(int simulation_id);

    // 시뮬레이션 종료
    public int endSimulation(int simulation_id);

    // 스코어 생성
    public int createScore();

    // 스코어 조회
    public Score searchScore(int score_id);

    // 스코어 수정 ( 게임 진행시 )
    public int updateScore(Score score);

    // 스코어 제거
    public int deleteScore(int score_id);

    // 타석정보 생성
    public int createHitInfo();

    // 타석정보 조회
    public HitInfo searchHitInfo(int hit_info_id);

    // 타석정보 수정 ( 게임 진행시 )
    public int updateHitInfo(HitInfo hit_info);

    // 타석정보 제거
    public int deleteHitInfo(int hit_info_id);

}
