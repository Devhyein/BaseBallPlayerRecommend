package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.LineupList;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.service.LineupService;
import com.ssafy.bigdata.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class LineupController {
    
    @Autowired 
    private LineupService lineupService;
    @Autowired
    private PlayerService PlayerService;


    @ApiOperation(value = "라인업 목록")
    @GetMapping("/lineupList")
    public Object getLineupList() {
        final RestResponse response = new RestResponse();
        List<LineupList>res = new ArrayList<LineupList>();

        List<Lineup> lineupList = lineupService.getLineupList();

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

    @ApiOperation(value = "라인업 선수 목록")
    @GetMapping("/lineup")
    public Object getLineupPlayer(@RequestParam int lineup) {
        final RestResponse response = new RestResponse();
        List<Player> res = new ArrayList<Player>();

        // 라인업의 선수 반환
        try {
            List<Integer> list = lineupService.getPlayerListByLineup(lineup);
                // 각 선수의 번호, 이름, 나이, 포지션, 팀
            for(int player_num : list){
                try {
                    res.add(PlayerService.searchPlayerById(player_num));
                    int index = 1; //타순 
                    if (res.size() > 0) {
                        for (Player p : res) {
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

        } catch(Exception e) {
            e.printStackTrace();
        }

        response.status = true;
        response.msg = "success";
        response.data = res;
        return response;
    }

    @ApiOperation(value = "라인업 등록")
    @PostMapping("/lineup/insert")
    public Object insertLineup(@RequestBody Lineup lineup) {
        final RestResponse response = new RestResponse();

        int res = lineupService.insertLineup(lineup);
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
