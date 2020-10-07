package com.ssafy.bigdata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Favorites;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.TeamStat;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.service.FavoritesService;
import com.ssafy.bigdata.service.PlayerServiceImpl;
import com.ssafy.bigdata.service.TeamService;
import com.ssafy.bigdata.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerServiceImpl playerServiceImpl;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "선수 리스트, 팀 스탯, 추천 선수")
    @GetMapping("/recommend1")
    public Object search_player(@RequestHeader final HttpHeaders header, @RequestParam int lineup, @RequestParam int option,
    @RequestParam(required = false) float power, @RequestParam(required = false) float speed, @RequestParam(required = false) float contact, 
    @RequestParam(required = false) float defense, @RequestParam(required = false) float shoulder,
    @RequestParam(required = false) float era, @RequestParam(required = false) float health, @RequestParam(required = false) float control, 
    @RequestParam(required = false) float stability, @RequestParam(required = false) float deterrent
    ) { // 지나치게 params가 많아지는 경우 post로 대체할 수도 있다고 하는데, 이미 get인걸 뜯어고치긴 좀 그러니까...
        // 슬라이더 값은 기본적으로 int가 아니라 4.00 같이 실수값인 듯

        final RestResponse response = new RestResponse();
        HashMap<String,Object>res = new HashMap<String,Object>();
        List<Player> playerlist = new ArrayList<Player>();
        List<Player> recommendlist = new ArrayList<Player>();

        ////////////////////////////////////////////////////////////////////
        ///////            토큰 해석
        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "NoToken";
            response.data = null;
            return response;
        }
        //////////////////////////////////////////////////////////////////////

        // 라인업의 선수 반환
        try {
            List<Integer> list = teamService.getPlayerListByLineup(lineup);
                // 각 선수의 번호, 이름, 나이, 포지션, 팀
            for(int player_num : list){
                try {
                    playerlist.add(playerDao.searchPlayerById(player_num));
                    int index = 1; //타순 
                    if (playerlist.size() > 0) {
                        for (Player p : playerlist) {
                            p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                            p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
                            p.setPlayer_age(playerServiceImpl.getAgeWithBirth(p.getPlayer_birth()));
                            p.setPlayer_position(index++);

                            Favorites favorites = new Favorites();
                            favorites.setPlayer_id(p.getPlayer_id());
                            favorites.setUser_id(user.getUser_id());

                            p.setIsFavorite(favoritesService.isFavorite(favorites));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        TeamStat data = new TeamStat();
        // 팀 분석 하기 위한 서비스 호출
        try{
            data = teamService.analyzeStat(lineup);
       
            System.out.println("TEAM STAT : "+ data);
            System.out.println("OPTION : " + option);
            // 여기서 분석한 데이터 -> 파이썬으로 전송

            // 팀 스탯 기반으로 weight 계산
            // weight 저장하는 DTO는 그냥 teamstat 갖다 씀 (어차피 둘 다 각 스탯에 대한 float값 저장하는 거라...)
            TeamStat weight = new TeamStat();
            weight.setTeam_id(data.getTeam_id());
            switch (option){
                case 1: // 1, 2번의 경우 팀스탯을 이용
                    weight.setPower(2 * (1 - data.getPower()) + 1);
                    weight.setSpeed(2 * (1 - data.getSpeed()) + 1);
                    weight.setContact(2 * (1 - data.getContact()) + 1);
                    weight.setDefense(2 * (1 - data.getDefense()) + 1);
                    weight.setShoulder(2 * (1 - data.getShoulder()) + 1);
                    weight.setEra(2 * (1 - data.getEra()) + 1);
                    weight.setHealth(2 * (1 - data.getHealth()) + 1);
                    weight.setControl(2 * (1 - data.getControl()) + 1);
                    weight.setStability(2 * (1 - data.getStability()) + 1);
                    weight.setDeterrent(2 * (1 - data.getDeterrent()) + 1);
                    break;
                case 2:
                    weight.setPower(2 * (data.getPower()) + 1);
                    weight.setSpeed(2 * (data.getSpeed()) + 1);
                    weight.setContact(2 * (data.getContact()) + 1);
                    weight.setDefense(2 * (data.getDefense()) + 1);
                    weight.setShoulder(2 * (data.getShoulder()) + 1);
                    weight.setEra(2 * (data.getEra()) + 1);
                    weight.setHealth(2 * (data.getHealth()) + 1);
                    weight.setControl(2 * (data.getControl()) + 1);
                    weight.setStability(2 * (data.getStability()) + 1);
                    weight.setDeterrent(2 * (data.getDeterrent()) + 1);
                    break;
                case 3: // 3번의 경우 param으로 가져온 값들을 이용
                    weight.setPower((power / 5) + 1);
                    weight.setSpeed((speed / 5) + 1);
                    weight.setContact((contact / 5) + 1);
                    weight.setDefense((defense / 5) + 1);
                    weight.setShoulder((shoulder / 5) + 1);
                    weight.setEra((era / 5) + 1);
                    weight.setHealth((health / 5) + 1);
                    weight.setControl((control / 5) + 1);
                    weight.setStability((stability / 5) + 1);
                    weight.setDeterrent((deterrent / 5) + 1);
                    break;
            }


            try {
                List<Integer> dat = (List<Integer>) sendToPython(weight).getBody().data;
                // HashMap<String,Object>res = new HashMap<String,Object>();
                for(int player_num : dat){
                    // 각 선수의 번호, 이름, 나이, 포지션, 팀
                    try {
                        recommendlist.add(playerDao.searchPlayerById(player_num));
                        if (recommendlist.size() > 0) {
                            for (Player p : recommendlist) {
                                p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                                p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
                                p.setPlayer_age(playerServiceImpl.getAgeWithBirth(p.getPlayer_birth()));
                                
                                Favorites favorites = new Favorites();
                                favorites.setPlayer_id(p.getPlayer_id());
                                favorites.setUser_id(user.getUser_id());

                                p.setIsFavorite(favoritesService.isFavorite(favorites));
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
        } catch(Exception e) {
            e.printStackTrace();
        }

        res.put("playerList", playerlist);
        res.put("teamStat", data);
        res.put("recommendList", recommendlist);
        res.put("token", userService.getTokenByEmail(user.getEmail()));


        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    // 파이썬에 팀 스탯 보내는 메소드
    // 가중치 옵션을 줄 수 있게 되어 요청 파라미터에 weight도 추가됨
    private ResponseEntity<RestResponse> sendToPython(TeamStat weight) throws IOException {
        final RestResponse result = new RestResponse();

        // 파이썬에 데이터 전송
        URL url = new URL("http://127.0.0.1:8000/api/recommend1?team_id=" + weight.getTeam_id() +"&power=" + weight.getPower()+ "&speed=" + weight.getSpeed()
            + "&contact=" + weight.getContact() + "&defense=" + weight.getDefense() + "&shoulder=" + weight.getShoulder()
            + "&era=" + weight.getEra() + "&health=" + weight.getHealth() + "&control=" + weight.getControl()
            + "&stability=" + weight.getStability() + "&deterrent=" + weight.getDeterrent());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Recommend Python server response code: " + responseCode);

        // 파이썬으로 부터 리턴
        if(responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String st = br.readLine();
            List<Integer> list = new ArrayList<Integer>();  
            System.out.println("** "+st);
            String digit = "";
            while (st.length()>0) {
                String ch = st.substring(0, 1);
                st = st.substring(1);
                if(Character.isDigit(ch.charAt(0))){
                    digit += ch;
                } else if(ch.charAt(0)==',' || ch.charAt(0)==']'){
                    if(digit.length() > 0) {
                        list.add(Integer.parseInt(digit));
                        digit = "";
                    }
                }
            }

            result.status = true;
            result.msg = "success";
            result.data = list;
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.msg = "Response code: " + responseCode;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    @ApiOperation(value = "선수 리스트로 팀 스탯 반환")
    @PostMapping("/recommend1/change")
    public Object search_player(@RequestHeader final HttpHeaders header, @RequestBody final Map<String, Object> request) {
        final RestResponse response = new RestResponse();

        /////////////////////////////////////////////////////////////////////
        ///////            토큰 해석
        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "NoToken";
            response.data = null;
            return response;
        }
        //////////////////////////////////////////////////////////////////////

        List<Integer> playerList = (List<Integer>) request.get("playerList");
        TeamStat data = new TeamStat();
        // 팀 분석 하기 위한 서비스 호출
        try{
            data = teamService.analyzeStatByPlayerList(playerList);
        } catch (Exception e){
            e.printStackTrace();
            response.status = false;
            response.msg = "failed";
            response.data = null;
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("teamStat", data);
        res.put("token", userService.getTokenByEmail(user.getEmail()));

        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }
}
