package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Object search_player() {
        final RestResponse response = new RestResponse();
        
        List<String> res = new ArrayList<String>();

        res = playerService.searchPlayerList();

        response.status = true;
        response.msg = "success";
        response.data = res;

        return response;
    }
}