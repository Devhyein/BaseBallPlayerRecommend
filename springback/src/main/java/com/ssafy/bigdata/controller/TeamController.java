package com.ssafy.bigdata.controller;

import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;
import com.ssafy.bigdata.service.PlayerService;
import com.ssafy.bigdata.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class TeamController {

    private TeamService teamService;

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @ApiOperation(value = "팀 분석")
    @GetMapping("/info/team/defaultstat")
    public Object search_player(@RequestParam int num) {
        final RestResponse response = new RestResponse();
        response.status = true;
        response.msg = "success";
        response.data = null;

        // 팀 분석 하기 위한 서비스 호출
        System.out.println(num);
        List<StatForChart>data = teamService.analyzeStat(num);

        // 여기서 분석한 데이터 -> 파이썬으로 전송

        // 파이썬에서 추천 결과 받아서 -> 프런트 전송

        // 결과값은 5툴만 보내는건강?? 팀의 5툴은 무엇일까
        return response;
    }
}
