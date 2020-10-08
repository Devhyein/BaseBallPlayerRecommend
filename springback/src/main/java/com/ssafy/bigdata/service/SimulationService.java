package com.ssafy.bigdata.service;

import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;

public interface SimulationService {

        // 시뮬레이션 생성
        public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack, int innings,
                        boolean is_top, int out_count, String base_info, String my_score, String your_score,
                        int my_hit_order, int your_hit_order, int game_status);

        // 시뮬레이션 조회
        public Simulation searchSimulation(int simulation_id);

        // 유저아이디로 시뮬레이션 아이디 조회
        public int searchSimulationByUserId(int user_id);

        // 시뮬레이션 진행
        public HashMap<String, Object> progressSimulation(Simulation simulation, int simulation_id, Score score,
                        List<Integer> my_lineup, List<Integer> your_lineup, User user);

        // 시뮬레이션 종료
        public int endSimulation(int simulation_id,User user);

        // 스코어 생성
        public int createScore(int simulation_id);

        // 스코어 조회
        public Score searchScore(int simulation_id);

        // 스코어 수정
        public int updateScore(Score score);

        // 타석정보 생성
        public int createHitInfo(int simulation_id, int player_id);

        // 타석정보 조회
        public HitInfo searchHitInfo(int simulation_id, int player_id);

        // 타석정보 수정
        public int updateHitInfo(HitInfo hit_info);

}
