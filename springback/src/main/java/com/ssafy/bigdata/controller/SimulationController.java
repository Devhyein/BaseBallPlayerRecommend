package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dao.user.UserDao;
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
    private UserDao userDao;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
   
    @ApiOperation(value = "유저 & 디폴트 라인업 목록")
    @GetMapping("/lineupList")
    public Object getUserLineupList(@RequestHeader final HttpHeaders header) {
        final RestResponse response = new RestResponse();
        List<LineupList> res = new ArrayList<LineupList>();
        User user = new User();
        // 토큰 해석
        String token = header.get("token").get(0);
        System.out.println("TOKEN : " + header.get("token").get(0));
     
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("*** "+jwtTokenProvider.getUserPk(token));
            user = userDao.findByEmail(jwtTokenProvider.getUserPk(token));
        } else {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "failed";
            response.data = null;
            return response;
        }

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
    
}
