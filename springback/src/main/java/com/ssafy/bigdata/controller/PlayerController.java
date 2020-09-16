package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;
import com.ssafy.bigdata.service.PlayerService;

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
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @ApiOperation(value = "선수 검색 리스트")
    @GetMapping("/info/playerlist")
    // 선수 이름 검색 시 해당 검색어 들어가는 선수 반환
    public Object search_player(@RequestParam final String search) {
        final RestResponse response = new RestResponse();
        
        List<Player> res = new ArrayList<Player>();
        try {
            res = playerService.searchPlayerList(search);
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

    @ApiOperation(value = "선수 스택")
    @GetMapping("/info/player")
    // 선수 이름 검색 시 해당 검색어 들어가는 선수 반환
    public Object playerStack(@RequestParam int num) {
        final RestResponse response = new RestResponse();
        
        // 선수 번호를 가지고 선수의 포지션 알아옴.
        String position = playerService.findPlayerPosition(num);
        HashMap<String,Object>res = new HashMap<String,Object>();
        // 투수면 ToolsPitcher, 타자면 ToolsHitter 선언
        System.out.println("POSITION : "+position);
        try {
            if(position.equals("투수")){
                res.put("five_tool", playerService.getPlayerStacksPitcher(num));
            } else {
                res.put("five_tool", playerService.getPlayerStacksHitter(num));
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
}