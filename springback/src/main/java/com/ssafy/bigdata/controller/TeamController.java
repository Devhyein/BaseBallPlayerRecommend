package com.ssafy.bigdata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;
import com.ssafy.bigdata.service.PlayerService;
import com.ssafy.bigdata.service.TeamService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        Object data = teamService.analyzeStat(num);
        System.out.println(data);
        // 여기서 분석한 데이터 -> 파이썬으로 전송
        try {
            sendToPython(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파이썬에서 추천 결과 받아서 -> 프런트 전송

        // 결과값은 5툴만 보내는건강?? 팀의 5툴은 무엇일까
        return response;
    }

    // 파이썬에 팀 스탯 보내는 메소드
    private ResponseEntity<RestResponse> sendToPython(Object data) throws IOException {
        final RestResponse result = new RestResponse();
        
        URL url = new URL("http://localhost:8903/api/recommend1?data=" + data);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Recommend Python server response code: " + responseCode);

        if(responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // JSONObject responseJson = new JSONObject(sb.toString());
            // JSONArray data = responseJson.getJSONArray("data");

            // ArrayList<Object> final_result = new ArrayList<Object>();
            // RestTemplate restTemplate = new RestTemplate();

            result.status = true;
            result.msg = "success";
            // result.data = final_result;
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.msg = "Response code: " + responseCode;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }
}
