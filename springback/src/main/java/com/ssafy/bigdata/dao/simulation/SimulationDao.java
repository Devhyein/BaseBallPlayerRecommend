package com.ssafy.bigdata.dao.simulation;

import com.ssafy.bigdata.dto.SimulationGame;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SimulationDao {

    // 시뮬레이션 조회
    public SimulationGame searchSimulation();

    // 시뮬레이션 생성
    public SimulationGame createSimulation();

    // 시뮬레이션 수정 ( 게임 진행시 )
    public SimulationGame updateSimulation();

    // 시뮬레이션 제거
    public SimulationGame deleteSimulation();

}
