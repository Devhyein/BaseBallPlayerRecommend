package com.ssafy.bigdata.controller;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;
import com.ssafy.bigdata.dto.simulation.SimulationData;
import com.ssafy.bigdata.service.LineupService;
import com.ssafy.bigdata.service.SimulationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring/game")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;
    private LineupService lineupService;

    @ApiOperation(value = "시뮬레이션 게임 시작")
    @PostMapping("/start")
    public Object simulationStart(@RequestBody int user_id, int my_lineup_id, int your_lineup_id, int is_attack) {
        RestResponse response = new RestResponse();
        Score score = null; // 스코어 정보를 담을 객체
        HitInfo hit_info = null; // 타석 정보를 담을 객체
        Lineup lineup = null; // 라인업 정보를 담을 객체
        Simulation simulation = null; // 시뮬레이션을 담을 객체
        int[] base_info_array = { 0, 0, 0 };

        try {
            int simulation_id = simulationService.createSimulation(user_id, my_lineup_id, your_lineup_id, is_attack, 1,
                    true, 0); // 시물레이션 생성
            List<Integer> my_lineup = lineupService.getPlayerListByLineup(simulation_id); // 내 라인업 정보
            List<Integer> your_lineup = lineupService.getPlayerListByLineup(your_lineup_id); // 상대 라인업 정보
            simulation = simulationService.searchSimulation(simulation_id); // 시뮬레이션 정보

            // response data
            try {
                int score_status = simulationService.createScore(simulation_id); // 점수 생성
                try {
                    score = simulationService.searchScore(simulation_id); // 스코어 정보
                } catch (Exception e) {
                    response.status = false;
                    response.msg = "Fail to create score board.";
                }
            } catch (Exception e) {
                response.status = false;
                response.msg = "Fail to load score score.";
            }

            try {
                int hit_info_status = simulationService.createHitInfo(simulation_id); // 타석정보 생성
                try {
                    hit_info = simulationService.searchHitInfo(simulation_id); // 타석 정보
                } catch (Exception e) {
                    response.status = false;
                    response.msg = "Fail to load hit_info board.";
                }
            } catch (Exception e) {
                response.status = false;
                response.msg = "Fail to create hit_info board.";
            }

            // simulate
            simulation = simulationService.progressSimulation(simulation_id); // 시뮬레이션 진행

            // data
            SimulationData data = new SimulationData(simulation, score, hit_info);

            response.status = true;
            response.msg = "success create simulation and play first innings";
            response.data = data;
        } catch (Exception e) {
            response.status = false;
            response.msg = "fail to start game";
        }

        return response;
    }

    @ApiOperation(value = "시뮬레이션 게임 진행")
    @PostMapping("/progress")
    public Object simulationProgress(@RequestBody int simulation_id, int replaced_player, int removed_player) {

        final RestResponse response = new RestResponse();

        try {
            response.status = true;
            response.msg = "success";
            response.data = null;
        } catch (Exception e) {
            response.status = false;
            response.msg = "fail to progress game";
        }

        return response;
    }

    @ApiOperation(value = "시뮬레이션 게임 종료")
    @GetMapping("/end")
    public Object simulationEnd(@RequestHeader HttpHeaders header) {
        final RestResponse response = new RestResponse();

        try {
            response.status = true;
            response.msg = "success";
            response.data = null;
        } catch (Exception e) {
            response.status = false;
            response.msg = "fail to end game";
        }

        return response;
    }

}
