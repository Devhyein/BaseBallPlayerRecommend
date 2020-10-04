package com.ssafy.bigdata.controller;

import java.util.HashMap;

import com.ssafy.bigdata.dto.RestResponse;
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

    @ApiOperation(value = "시뮬레이션 게임 시작")
    @PostMapping("/start")
    public Object simulationStart(@RequestBody HashMap<String, Object> request) {
        final RestResponse response = new RestResponse();

        try {
            response.status = true;
            response.msg = "success";
            response.data = null;
        } catch (Exception e) {
            response.status = false;
            response.msg = "fail to start game";
        }

        return response;
    }

    @ApiOperation(value = "시뮬레이션 게임 진행")
    @PostMapping("/progress")
    public Object simulationProgress(@RequestBody HashMap<String, Object> request) {
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
