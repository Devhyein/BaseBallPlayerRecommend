package com.ssafy.bigdata.service;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dao.team.TeamDao;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {
    private TeamDao teamDao;
    private PlayerDao playerDao;
    private PlayerService playerService;

    @Autowired
    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public List<StatForChart> analyzeStat(int num) {
            
        // 1. 이 팀의 최신 라인업 불러오기
        try {            
            Lineup lineup = teamDao.getLineupByTeamId(num);
            
            // 2. 라인업의 플레이어들을 검색해서 record기록 가져오기
            // 이 레코드는 5개만
            // 타자의 5개 + 투수의 5개 해서 10개 데이터

            // 타자 1번 ~ 9번까지의 레코드 가져와서 5툴 가져오기
            List<ToolsHitter> toolsHitter = new ArrayList<ToolsHitter>();
            List<ToolsPitcher> toolsPitcher = new ArrayList<ToolsPitcher>();
            // 레코드 가져와서 5툴 계산
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter1())
                , playerDao.getPlayerStackFielder(lineup.getHitter1())));
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter2())
                , playerDao.getPlayerStackFielder(lineup.getHitter2())));
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter3())
                , playerDao.getPlayerStackFielder(lineup.getHitter3()))); 
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter4())
                , playerDao.getPlayerStackFielder(lineup.getHitter4()))); 
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter5())
                , playerDao.getPlayerStackFielder(lineup.getHitter5()))); 
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter6())
                , playerDao.getPlayerStackFielder(lineup.getHitter6()))); 
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter7())
                , playerDao.getPlayerStackFielder(lineup.getHitter7()))); 
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter8())
                , playerDao.getPlayerStackFielder(lineup.getHitter8())));   
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStacksHitter(lineup.getHitter9())
                , playerDao.getPlayerStackFielder(lineup.getHitter9())));  
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                toolsPitcher.add(playerService.calculateToolsPitcher(playerDao.getPlayerStacksPitcher(lineup.getPitcher())));
            } catch(Exception e){
                e.printStackTrace();
            }
            
            // 3. record 표준화
            // 타자 5툴 각각 for문 돌면서 각각 다 더한 후 나눠주기 -> 평균
            float speed = 0;
            float contact = 0;
            float defense = 0;
            float power = 0;
            float shoulder = 0;
            for (ToolsHitter hitter : toolsHitter){
                speed += hitter.getSpeed();
                contact += hitter.getContact();
                defense += hitter.getDefense();
                power += hitter.getPower();
                shoulder += hitter.getShoulder();
            }

            speed = speed/toolsHitter.size();
            contact = contact/toolsHitter.size();
            defense = defense/toolsHitter.size();
            power = power/toolsHitter.size();
            shoulder = shoulder/toolsHitter.size();

            System.out.println("speed: "+speed+" contact: "+contact+" defense: "+defense+" power: "+power+" shoulder: "+shoulder);
                
            // 4. 팀 전체의 평균 스탯 분석

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }


        return null;
    }

        

}
