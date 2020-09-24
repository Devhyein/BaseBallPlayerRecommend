package com.ssafy.bigdata.service;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dao.team.TeamDao;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.TeamStat;
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
    public TeamStat analyzeStat(int num) {
        TeamStat stat = new TeamStat();
        // 1. 이 팀의 최신 라인업 불러오기
        try {            
            Lineup lineup = teamDao.getPlayerListByLineup(num);
            System.out.println("LINEUP : "+lineup);
            // 2. 라인업의 플레이어들을 검색해서 record기록 가져오기
            // 이 레코드는 5개만
            // 타자의 5개 + 투수의 5개 해서 10개 데이터

            // 타자 1번 ~ 9번까지의 레코드 가져와서 5툴 가져오기
            List<ToolsHitter> toolsHitter = new ArrayList<ToolsHitter>();
            ToolsPitcher toolsPitcher = new ToolsPitcher();
            // 레코드 가져와서 5툴 계산
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter1())
                , playerDao.getPlayerStatsFielder(lineup.getHitter1())));
            } catch(Exception e){
                // System.out.println("no data");
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter2())
                , playerDao.getPlayerStatsFielder(lineup.getHitter2())));
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter3())
                , playerDao.getPlayerStatsFielder(lineup.getHitter3()))); 
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter4())
                , playerDao.getPlayerStatsFielder(lineup.getHitter4()))); 
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter5())
                , playerDao.getPlayerStatsFielder(lineup.getHitter5()))); 
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter6())
                , playerDao.getPlayerStatsFielder(lineup.getHitter6()))); 
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter7())
                , playerDao.getPlayerStatsFielder(lineup.getHitter7()))); 
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter8())
                , playerDao.getPlayerStatsFielder(lineup.getHitter8())));   
            } catch(Exception e){
            }
            try {
                toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(lineup.getHitter9())
                , playerDao.getPlayerStatsFielder(lineup.getHitter9())));  
            } catch(Exception e){
            }

            try {
                toolsPitcher = playerService.calculateToolsPitcher(playerDao.getPlayerStatsPitcher(lineup.getPitcher()));
            } catch(Exception e){
            }
            
            // 3. record 표준화
            // 타자 5툴 각각 for문 돌면서 각각 다 더한 후 나눠주기 -> 평균
            System.out.println("^^^^^"+toolsHitter.size());
            if(toolsHitter.size()>0){
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
                stat.setTeam_id(num);


                stat.setSpeed(speed/toolsHitter.size());
                stat.setContact(contact/toolsHitter.size());
                stat.setDefense(defense/toolsHitter.size());
                stat.setPower(power/toolsHitter.size());
                stat.setShoulder(shoulder/toolsHitter.size());
            } else {
                stat.setSpeed((float) 0.0);
                stat.setContact((float) 0.0);
                stat.setDefense((float) 0.0);
                stat.setPower((float) 0.0);
                stat.setShoulder((float) 0.0);
            }



    
            stat.setEra(toolsPitcher.getEra());
            stat.setHealth(toolsPitcher.getHealth());
            stat.setControl(toolsPitcher.getControl());
            stat.setStability(toolsPitcher.getStability());
            stat.setDeterrent(toolsPitcher.getDeterrent());

            return stat;
            // 4. 팀 전체의 평균 스탯 분석

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public TeamStat analyzeStatByPlayerList(List<Integer> playerList) {
        TeamStat stat = new TeamStat();
        // 1. 이 팀의 최신 라인업 불러오기
        try {            
            List<ToolsHitter> toolsHitter = new ArrayList<ToolsHitter>();
            ToolsPitcher toolsPitcher = new ToolsPitcher();

            for(int player : playerList){
                System.out.println("# "+player);
                System.out.println(playerDao.findPlayerPosition(player));
                if(playerDao.findPlayerPosition(player).equals("투수")){
                    try {
                        toolsPitcher = playerService.calculateToolsPitcher(playerDao.getPlayerStatsPitcher(player));
                    } catch(Exception e){
                    }
                } else {
                    try{
                        toolsHitter.add(playerService.calculateToolsHitter(playerDao.getPlayerStatsHitter(player)
                        , playerDao.getPlayerStatsFielder(player)));
                    } catch (Exception e) {

                    }
                }
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
            stat.setSpeed(speed/toolsHitter.size());
            stat.setContact(contact/toolsHitter.size());
            stat.setDefense(defense/toolsHitter.size());
            stat.setPower(power/toolsHitter.size());
            stat.setShoulder(shoulder/toolsHitter.size());

    
            stat.setEra(toolsPitcher.getEra());
            stat.setHealth(toolsPitcher.getHealth());
            stat.setControl(toolsPitcher.getControl());
            stat.setStability(toolsPitcher.getStability());
            stat.setDeterrent(toolsPitcher.getDeterrent());

            return stat;
            // 4. 팀 전체의 평균 스탯 분석

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Integer> getPlayerListByLineup(int lineup) throws Exception{
        List<Integer> res = new ArrayList<>();
        Lineup line=teamDao.getPlayerListByLineup(lineup);
        res.add(line.getHitter1());        
        res.add(line.getHitter2());
        res.add(line.getHitter3());
        res.add(line.getHitter4());
        res.add(line.getHitter5());
        res.add(line.getHitter6());
        res.add(line.getHitter7());
        res.add(line.getHitter8());
        res.add(line.getHitter9());
        res.add(line.getPitcher());
        return res;
    }

    @Override
    public List<Lineup> getLineupList() {
        List<Lineup> lineup = teamDao.getLineup();
        
        return lineup;
    }

        

}
