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
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.LineupList;
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

    private TeamService teamService;
    private PlayerServiceImpl playerServiceImpl;
    private PlayerDao playerDao;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private UserService userService;

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
    
    @ApiOperation(value = "라인업 목록")
    @GetMapping("/team/lineup")
    public Object get_lineupList() {
        final RestResponse response = new RestResponse();
        List<LineupList>res = new ArrayList<LineupList>();

        List<Lineup> lineupList = teamService.getLineupList();

        for(Lineup list : lineupList){
            LineupList lineup = new LineupList();
            lineup.setId(list.getLineup_id());
            lineup.setName(list.getLineup_name());
            res.add(lineup);
        }

        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }


    @ApiOperation(value = "선수 리스트, 팀 스탯, 추천 선수")
    @GetMapping("/recommend1")
    public Object search_player(@RequestHeader final HttpHeaders header, @RequestParam int lineup) {
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
            response.msg = "Token Failed";
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
       
            System.out.println("TEAM STAT : "+data);
            // 여기서 분석한 데이터 -> 파이썬으로 전송
            try {
                List<Integer> dat = (List<Integer>) sendToPython(data).getBody().data;
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


        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    // 파이썬에 팀 스탯 보내는 메소드
    private ResponseEntity<RestResponse> sendToPython(TeamStat data) throws IOException {
        final RestResponse result = new RestResponse();

        // 파이썬에 데이터 전송
        URL url = new URL("http://127.0.0.1:8000/api/recommend1?team_id=" + data.getTeam_id() +"&power=" + data.getPower()+ "&speed=" + data.getSpeed()
            + "&contact=" + data.getContact() + "&defense=" + data.getDefense() + "&shoulder=" + data.getShoulder()
            + "&era=" + data.getEra() + "&health=" + data.getHealth() + "&control=" + data.getControl()
            + "&stability=" + data.getStability() + "&deterrent=" + data.getDeterrent());
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
    public Object search_player(@RequestBody final Map<String, Object> request) {
        final RestResponse response = new RestResponse();
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
        response.status = true;
        response.msg = "success";
        response.data = data;
        return response;
    }
}
