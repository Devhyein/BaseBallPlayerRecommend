package com.ssafy.bigdata.service;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RecordFielder;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerDao playerDao;

    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public List<Player> searchPlayerList(String search) {
        try {
            List<Player> playerlist = playerDao.searchPlayerList(search);
            if (playerlist.size() > 0) {
                for (Player p : playerlist) {
                    p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                }
            }
            System.out.println(playerlist.get(0).toString());
            return playerlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String findPlayerPosition(int num) {
        try {
            String position = playerDao.findPlayerPosition(num);
            return position;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Object> getPlayerStacksPitcher(int num) {
        List<Object> res = new ArrayList<Object>();
        try {
            RecordPitcher record = playerDao.getPlayerStacksPitcher(num);
            // 5 tool
            ToolsPitcher five_tool = calculateToolsPitcher(record);
            res.add(five_tool);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // 투수의 5툴 계산하는 메소드
    /*
     * 평균자책점 = ERA+ or FIP+ (높을수록 좋음) 
     * 체력 (이닝소화력) = 이닝 / 경기수 (높을수록 좋음) 
     * 제구력: SO/BB (높을수록 좋음) 
     * 안정성: 폭투, 보크 (낮을수록 좋음)
     * 장타 억제력?: 홈런 (낮을수록 좋음)
     */
    public ToolsPitcher calculateToolsPitcher(RecordPitcher record) {
        ToolsPitcher tools = new ToolsPitcher();
        
        tools.setEra((float) (record.getPitcher_era_plus() / 370.4));

        int game = record.getPitcher_g() * 9;
        float inning = record.getPitcher_ip();
        if (game != 0)
            tools.setHealth((float) ((inning / game) / 67.68));
        else
            tools.setHealth(0);

        if (record.getPitcher_bb() != 0) {
            float controll = record.getPitcher_so() / (record.getPitcher_bb());
            tools.setControll(controll / 12);
        } else {
            tools.setControll(0);
        }

        float stab = record.getPitcher_bk() + record.getPitcher_wp();
        tools.setStability(1 - stab / 20);

        float de = record.getPitcher_homerun();
        tools.setDeterrent(1 - de / 31);

        //System.out.println(tools);
        return tools;
    }

    @Override
    public List<Object> getPlayerStacksHitter(int num) {
        List<Object> res = new ArrayList<Object>();
        try {
            RecordHitter recordHitter = playerDao.getPlayerStacksHitter(num);
            RecordFielder recordFielder = playerDao.getPlayerStackFielder(num);
            // 5 tool
            ToolsHitter five_tool = calculateToolsHitter(recordHitter, recordFielder);
            res.add(five_tool);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // 타자 5툴 계산하는 메소드
    /*
     * 파워: 장타율, 홈런
     * 스피드: 도루, 도루성공율 (도루/도루+도실), 3루타 
     * 컨택: 타율, BB/K 수비: 수비율, RNG 
     * 어깨(송구능력): 보살, ARM (외야수), CS (포수)
     */
    public ToolsHitter calculateToolsHitter(RecordHitter hitter, RecordFielder fielder) {
        ToolsHitter res = new ToolsHitter();

        res.setPower((float) ((hitter.getHitter_homerum() + hitter.getHitter_slg()) / 53.714));

        float sp = hitter.getHitter_cs() + hitter.getHitter_sb();
        float speed1 = 0;
        if (sp != 0) {
            speed1 = hitter.getHitter_sb() / sp;
        }
        float speed2 = 0;
        if (hitter.getHitter_ab() != 0) {
            speed2 = hitter.getHitter_triple() / hitter.getHitter_ab();
        }
        res.setSpeed((float) ((speed1 + speed2) / 1.1));

        float controll = 0;
        if (hitter.getHitter_pa() != 0) {
            controll = (hitter.getHitter_bb() - hitter.getHitter_so()) / hitter.getHitter_pa();
        }
        res.setContact(hitter.getHitter_ba() + controll);

        float defense = fielder.getFielder_fld() + fielder.getFielder_rng();
        res.setDefense((float) (defense / 22.24));

        float shoulder = fielder.getFielder_a()/451;
        // 여기 fielder.getFielder_id -> getPlayer_id 로 수정했어여..
        String position = playerDao.findPlayerPosition(fielder.getPlayer_id());
        if (position.equals("포수")) {
            shoulder += (fielder.getFielder_cs()+2.8)/5.83;
        } else if (position.equals("좌익수") || position.equals("중견수") || position.equals("우익수")) {
            shoulder += (fielder.getFielder_arm()+6.12)/13.24;
        }
        res.setShoulder(shoulder);
        //System.out.println(res);
        return res;
    }

}
