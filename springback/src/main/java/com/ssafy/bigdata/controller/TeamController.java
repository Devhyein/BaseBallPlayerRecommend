package com.ssafy.bigdata.controller;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;
import com.ssafy.bigdata.service.PlayerService;
import com.ssafy.bigdata.service.PlayerServiceImpl;
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
    private PlayerServiceImpl playerServiceImpl;
    private PlayerDao playerDao;

    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }
    
    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setPlayerServiceImpl(PlayerServiceImpl playerServiceImpl) {
        this.playerServiceImpl = playerServiceImpl;
    }

    @ApiOperation(value = "팀 분석")
    @GetMapping("/info/team/defaultstat")
    public Object search_player(@RequestParam int num) {
        final RestResponse response = new RestResponse();

        // 팀 분석 하기 위한 서비스 호출
        Map<String,Object> data = teamService.analyzeStat(num);
        List<Player> playerlist = new ArrayList<Player>();

        // 여기서 분석한 데이터 -> 파이썬으로 전송
        try {
            List<Integer> dat = (List<Integer>) sendToPython(data).getBody().data;
            // HashMap<String,Object>res = new HashMap<String,Object>();
            for(int player_num : dat){
                // 각 선수의 번호, 이름, 나이, 포지션, 팀
                try {
                    playerlist.add(playerDao.searchPlayerById(player_num));
                    if (playerlist.size() > 0) {
                        for (Player p : playerlist) {
                            p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                            p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
                            p.setPlayer_age(playerServiceImpl.getAgeWithBirth(p.getPlayer_birth()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.status = false;
            response.msg = "connection error";
            response.data = null;
        }
        response.status = true;
        response.msg = "success";
        response.data = playerlist;
        return response;
    }

    // 파이썬에 팀 스탯 보내는 메소드
    private ResponseEntity<RestResponse> sendToPython(Map<String,Object> data) throws IOException {
        final RestResponse result = new RestResponse();

        // 파이썬에 데이터 전송
        URL url = new URL("http://127.0.0.1:8000/api/recommend1?team_id=" + data.get("team_id") +"&power=" + data.get("power")+ "&speed=" + data.get("speed")
            + "&contact=" + data.get("contact") + "&defense=" + data.get("defense") + "&shoulder=" + data.get("shoulder")
            + "&era=" + data.get("era") + "&health=" + data.get("health") + "&control=" + data.get("control")
            + "&stability=" + data.get("stability") + "&deterrent=" + data.get("deterrent"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Recommend Python server response code: " + responseCode);

        // 파이썬으로 부터 리턴
        if(responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            List<Integer> recom_player = new ArrayList<Integer>();
            // while()

            // 여기서 부터 데이터 받아서 recom_player에 추가하는 작업
            
            // 임시 데이터
            recom_player.add(4107594);
            recom_player.add(45076805);
            recom_player.add(8151975);
            recom_player.add(67440725);
            recom_player.add(66368441);

            result.status = true;
            result.msg = "success";
            result.data = recom_player;
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.msg = "Response code: " + responseCode;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }
}
