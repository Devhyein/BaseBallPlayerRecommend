package com.ssafy.bigdata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Favorites;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.SearchRequest;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.TeamStat;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.service.FavoritesService;
import com.ssafy.bigdata.service.PlayerService;
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
public class PlayerController {

    private PlayerService playerService;
    private TeamService teamService;
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
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @ApiOperation(value = "선수 검색 리스트")
    @PostMapping("/info/playerlist")
    // 선수 이름 검색 시 해당 검색어 들어가는 선수 반환
    public Object search_player(@RequestHeader final HttpHeaders header, @RequestBody final SearchRequest search) {
        final RestResponse response = new RestResponse();

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
        
        List<Player> res = new ArrayList<Player>();
        try {
            res = playerService.searchPlayerList(search);

            for(Player player : res) {
                Favorites favorites = new Favorites();
                favorites.setPlayer_id(player.getPlayer_id());
                favorites.setUser_id(user.getUser_id());

                player.setIsFavorite(favoritesService.isFavorite(favorites));
            }

            response.status = true;
            response.msg = "success";
            response.data = res;
        } catch (Exception e){
            response.status = false;
            response.msg = "search player list error";
            response.data = null;
        }
        return response;
    }

    @ApiOperation(value = "선수 스탯")
    @GetMapping("/info/player")
    public Object playerStat(@RequestParam int num) {
        final RestResponse response = new RestResponse();
        
        // 선수 번호를 가지고 선수의 포지션 알아옴.
        String position = playerService.findPlayerPosition(num);
        HashMap<String,Object>res = new HashMap<String,Object>();
        List<StatForChart> statList = new ArrayList<StatForChart>();
        // 투수면 ToolsPitcher, 타자면 ToolsHitter 선언
        System.out.println("POSITION : "+position);
        try {
            if(position.equals("투수")){
                res.put("five_tool", playerService.getPlayerToolsPitcher(num));
                res.put("stats", playerService.getPlayerStatsPitcher(num));
            } else {
                res.put("five_tool", playerService.getPlayerToolsHitter(num));
                statList = playerService.getPlayerStatsHitter(num);
                statList.addAll(playerService.getPlayerStatsFielder(num));
    
                res.put("stats", statList);
            }   
        } catch (Exception e) {
            response.status = false;
            response.msg = "No data";
            response.data = null;
            return response;
        }
        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    @ApiOperation(value = "플레이어별 추천 탭 석택시 라인업리스트, 팀스탯정보 제공")
    @GetMapping("/recommend2")
    public Object search_player(@RequestHeader final HttpHeaders header, @RequestParam int lineup) {
        final RestResponse response = new RestResponse();
        HashMap<String,Object>res = new HashMap<String,Object>();
        List<Player> playerlist = new ArrayList<Player>();

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
                            p.setPlayer_age(playerService.getAgeWithBirth(p.getPlayer_birth()));
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
        // 팀분석
        TeamStat data = new TeamStat();
        try {
            data = teamService.analyzeStat(lineup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        res.put("playerList", playerlist);
        res.put("teamStat", data);

        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    @ApiOperation(value = "플레이어별 추천 탭 석택시 라인업리스트, 팀스탯정보 제공")
    @GetMapping("/recommend/player")
    public Object recommend_player(@RequestHeader final HttpHeaders header, @RequestParam int player_id) {
        final RestResponse response = new RestResponse();
        HashMap<String,Object>res = new HashMap<String,Object>();
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

        try {
            List<Integer> dat = (List<Integer>) sendToPython(player_id).getBody().data;
            for(int player_num : dat){
                // 각 선수의 번호, 이름, 나이, 포지션, 팀
                try {
                    recommendlist.add(playerDao.searchPlayerById(player_num));
                    if (recommendlist.size() > 0) {
                        for (Player p : recommendlist) {
                            p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                            p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
                            p.setPlayer_age(playerService.getAgeWithBirth(p.getPlayer_birth()));

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
      
        res.put("recommendList", recommendlist);

        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    // 파이썬에 플레이어 id 보내는 메소드
    private ResponseEntity<RestResponse> sendToPython(int player_id) throws IOException {
        final RestResponse result = new RestResponse();

        // 파이썬에 데이터 전송
        URL url = new URL("http://127.0.0.1:8000/api/recommend2?player_id=" + player_id);
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
            String line = "";          
            String digit = "";
            while (st.length()>0) {
                String ch = st.substring(0, 1);
                st = st.substring(1);
                if(Character.isDigit(ch.charAt(0))){
                    digit += ch.charAt(0);
                } else if(ch.charAt(0)==',' || ch.charAt(0)==']'){
                    list.add(Integer.parseInt(digit));
                    digit = "";
                }
                line+=ch;
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
}