package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.LineupList;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.TeamStat;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.JwtTokenProvider;
import com.ssafy.bigdata.service.LineupService;
import com.ssafy.bigdata.service.PlayerService;
import com.ssafy.bigdata.service.TeamService;
import com.ssafy.bigdata.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class LineupController {

    @Autowired
    private LineupService lineupService;
    @Autowired
    private PlayerService PlayerService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
   
    @ApiOperation(value = "유저 & 디폴트 라인업 목록")
    @GetMapping("/lineupList")
    public Object getUserLineupList(@RequestHeader final HttpHeaders header) {
        final RestResponse response = new RestResponse();
        List<LineupList> res = new ArrayList<LineupList>();
        
        /////////////////////////////////////////////////////////////////////
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

        List<Lineup> lineupList = lineupService.getUserLineupList(user.getUser_id());

        for (Lineup list : lineupList) {
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

    @ApiOperation(value = "라인업 선수 목록")
    @GetMapping("/lineup")
    public Object getLineupPlayer(@RequestParam int lineup) {
        final RestResponse response = new RestResponse();
        HashMap<String, Object> res = new HashMap<String, Object>();
        List<Player> playerlist = new ArrayList<Player>();

        // 라인업의 선수 반환
        try {
            List<Integer> list = lineupService.getPlayerListByLineup(lineup);
            // 각 선수의 번호, 이름, 나이, 포지션, 팀
            for (int player_num : list) {
                try {
                    playerlist.add(PlayerService.searchPlayerById(player_num));
                    int index = 1; // 타순
                    if (playerlist.size() > 0) {
                        for (Player p : playerlist) {
                            p.setPlayer_team(PlayerService.findTeamName(p.getTeam_id()));
                            p.setPosition(PlayerService.findPlayerPosition(p.getPlayer_id()));
                            p.setPlayer_age(PlayerService.getAgeWithBirth(p.getPlayer_birth()));
                            p.setPlayer_position(index++);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
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

    @ApiOperation(value = "라인업 등록")
    @PostMapping("/lineup/insert")
    public Object insertLineup(@RequestBody HashMap<String, Object> request) {
        final RestResponse response = new RestResponse();
        int res=0;
        try{
            List<Object> up = (List<Object>) request.get("lineup"); 
            String lineup_name = (String) up.get(0);
            int hitter1 = (int) up.get(1);
            int hitter2 = (int) up.get(2);
            int hitter3 = (int) up.get(3);
            int hitter4 = (int) up.get(4);
            int hitter5 = (int) up.get(5);
            int hitter6 = (int) up.get(6);
            int hitter7 = (int) up.get(7);
            int hitter8 = (int) up.get(8);
            int hitter9 = (int) up.get(9);
            int pitcher = (int) up.get(10);
            res = lineupService.insertLineup(lineup_name, hitter1, hitter2, hitter3, hitter4, hitter5, hitter6, hitter7, hitter8, hitter9, pitcher,(int) request.get("user_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(res!=0){
            response.status = true;
            response.msg = "success";
            response.data = res;
        }else{
            response.status = false;
            response.msg = "fail to insert lineup";
            response.data = res;
        }
        
        return response;
    }

    @ApiOperation(value = "라인업 수정")
    @PutMapping("/lineup/update")
    public Object updateLineup(@RequestBody Lineup lineup) {
        final RestResponse response = new RestResponse();

        int res = lineupService.updateLineup(lineup);
        if(res!=0){
            response.status = true;
            response.msg = "success";
            response.data = res;
        }else{
            response.status = false;
            response.msg = "fail to update lineup";
            response.data = res;
        }

        return response;
    }

    @ApiOperation(value = "라인업 삭제")
    @DeleteMapping("/lineup/delete")
    public Object updateLineup(@RequestParam int lineup) {
        final RestResponse response = new RestResponse();

        int res = lineupService.deleteLineup(lineup);
        if(res!=0){
            response.status = true;
            response.msg = "success";
            response.data = res;
        }else{
            response.status = false;
            response.msg = "fail to delete lineup";
            response.data = res;
        }

        return response;
    }
    
}
