package com.ssafy.bigdata.service;

import java.util.ArrayList;
import java.util.Calendar;
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
                    p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
                    p.setPlayer_age(getAgeWithBirth(p.getPlayer_birth()));
                }
            }
            return playerlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getAgeWithBirth(String birth){
        int age = 0;
        String[] ymd = birth.split("-");
        int year = Integer.parseInt(ymd[0]);
        age = Calendar.getInstance().get(Calendar.YEAR)-year+1;
        return age;
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
    public ToolsPitcher getPlayerToolsPitcher(int num) throws Exception{
        
        RecordPitcher record = playerDao.getPlayerStatsPitcher(num);
        // 5 tool
        ToolsPitcher five_tool = calculateToolsPitcher(record);
        
        return five_tool;
    }

    // 투수의 5툴 계산하는 메소드
    /*
     * 평균자책점 = ERA+ or FIP+ (높을수록 좋음) 
     * 체력 (이닝소화력) = 이닝 / 경기수 (높을수록 좋음) 
     * 제구력: SO/BB (높을수록 좋음) 
     * 안정성: 폭투, 보크 (낮을수록 좋음)
     * 장타 억제력?: 홈런 (낮을수록 좋음)
     */
    @Override
    public ToolsPitcher calculateToolsPitcher(RecordPitcher record) throws Exception{
        ToolsPitcher tools = new ToolsPitcher();
        
        tools.setEra((float) (Math.round((record.getPitcher_era_plus() / 370.4) * 100) / 100.0));

        int game = record.getPitcher_g() * 9;
        float inning = record.getPitcher_ip();
        if (game != 0)
            tools.setHealth((float) (Math.round(((inning / game) / 67.68) * 100) / 100.));
        else
            tools.setHealth(0);

        if (record.getPitcher_bb() != 0) {
            float control = record.getPitcher_so() / (record.getPitcher_bb());
            tools.setControl((float) (Math.round((control / 12) * 100) / 100.0));
        } else {
            tools.setControl(0);
        }

        float stab = record.getPitcher_bk() + record.getPitcher_wp();
        tools.setStability((float) (Math.round((1 - stab / 20) * 100) / 100.0));

        float de = record.getPitcher_homerun();
        tools.setDeterrent((float) (Math.round((1 - de / 31) * 100) / 100.0));

        //System.out.println(tools);
        return tools;
    }

    @Override
    public ToolsHitter getPlayerToolsHitter(int num) throws Exception{
      
        RecordHitter recordHitter = playerDao.getPlayerStatsHitter(num);
        RecordFielder recordFielder = playerDao.getPlayerStatsFielder(num);
        // 5 tool
        ToolsHitter five_tool = calculateToolsHitter(recordHitter, recordFielder);
       
        return five_tool;
    }

    // 타자 5툴 계산하는 메소드
    /*
     * 파워: 장타율, 홈런
     * 스피드: 도루, 도루성공율 (도루/도루+도실), 3루타 
     * 컨택: 타율, BB/K 수비: 수비율, RNG 
     * 어깨(송구능력): 보살, ARM (외야수), CS (포수)
     */
    @Override
    public ToolsHitter calculateToolsHitter(RecordHitter hitter, RecordFielder fielder) throws Exception{
        ToolsHitter res = new ToolsHitter();

        res.setPower((float) (Math.round(((hitter.getHitter_homerun() + hitter.getHitter_slg()) / 53.714) * 100) / 100.0));

        float sp = hitter.getHitter_cs() + hitter.getHitter_sb();
        float speed1 = 0;
        if (sp != 0) {
            speed1 = hitter.getHitter_sb() / sp;
        }
        float speed2 = 0;
        if (hitter.getHitter_ab() != 0) {
            speed2 = hitter.getHitter_triple() / hitter.getHitter_ab();
        }
        res.setSpeed((float) (Math.round(((speed1 + speed2) / 1.1) * 100) / 100.0));

        float control = 0;
        if (hitter.getHitter_pa() != 0) {
            control = (hitter.getHitter_bb() - hitter.getHitter_so()) / hitter.getHitter_pa();
        }
        res.setContact((float) (Math.round((hitter.getHitter_ba() + control) * 100) / 100.0));

        float defense = fielder.getFielder_fld() + fielder.getFielder_rng();
        res.setDefense((float) (Math.round((defense / 22.24)*100)/100.0));

        float shoulder = fielder.getFielder_a()/451;
        // 여기 fielder.getFielder_id -> getPlayer_id 로 수정했어여..
        String position = playerDao.findPlayerPosition(fielder.getPlayer_id());
        if (position.equals("포수")) {
            shoulder += (fielder.getFielder_cs()+2.8)/5.83;
        } else if (position.equals("좌익수") || position.equals("중견수") || position.equals("우익수")) {
            shoulder += (fielder.getFielder_arm()+6.12)/13.24;
        }
        res.setShoulder((float) (Math.round(shoulder * 100) / 100.0));
        //System.out.println(res);
        return res;
    }

    //Stats 정보 제공
    @Override
    public List<StatForChart> getPlayerStatsPitcher(int num) throws Exception{
        RecordPitcher record = playerDao.getPlayerStatsPitcher(num);
        List<StatForChart> statList = new ArrayList<StatForChart>();
        StatForChart stats = new StatForChart();
    
        stats.setStat_name("출장");
        stats.setStat_value(record.getPitcher_g());
        stats.setStat_std(calculateStatsPitcher("pitcher_g",record.getPitcher_g()));
        statList.add(stats);
       
        stats = new StatForChart();
        stats.setStat_name("완투");
        stats.setStat_value(record.getPitcher_cg());
        stats.setStat_std(calculateStatsPitcher("pitcher_cg",record.getPitcher_cg()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("완봉");
        stats.setStat_value(record.getPitcher_sho());
        stats.setStat_std(calculateStatsPitcher("pitcher_sho",record.getPitcher_sho()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("선발");
        stats.setStat_value(record.getPitcher_gs());
        stats.setStat_std(calculateStatsPitcher("pitcher_gs",record.getPitcher_gs()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("승");
        stats.setStat_value(record.getPitcher_w());
        stats.setStat_std(calculateStatsPitcher("pitcher_w",record.getPitcher_w()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("패");
        stats.setStat_value(record.getPitcher_l());
        stats.setStat_std(calculateStatsPitcher("pitcher_l",record.getPitcher_l()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("세");
        stats.setStat_value(record.getPitcher_sv());
        stats.setStat_std(calculateStatsPitcher("pitcher_sv",record.getPitcher_sv()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("홀드");
        stats.setStat_value(record.getPitcher_hld());
        stats.setStat_std(calculateStatsPitcher("pitcher_hld",record.getPitcher_hld()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("이닝");
        stats.setStat_value(record.getPitcher_ip());
        stats.setStat_std(calculateStatsPitcher("pitcher_ip",record.getPitcher_ip()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("실점");
        stats.setStat_value(record.getPitcher_r());
        stats.setStat_std(calculateStatsPitcher("pitcher_r",record.getPitcher_r()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("자책");
        stats.setStat_value(record.getPitcher_er());
        stats.setStat_std(calculateStatsPitcher("pitcher_er",record.getPitcher_er()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("안타");
        stats.setStat_value(record.getPitcher_h());
        stats.setStat_std(calculateStatsPitcher("pitcher_h",record.getPitcher_h()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("2타");
        stats.setStat_value(record.getPitcher_2b());
        stats.setStat_std(calculateStatsPitcher("pitcher_2b",record.getPitcher_2b()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("3타");
        stats.setStat_value(record.getPitcher_3b());
        stats.setStat_std(calculateStatsPitcher("pitcher_3b",record.getPitcher_3b()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("홈런");
        stats.setStat_value(record.getPitcher_homerun());
        stats.setStat_std(calculateStatsPitcher("pitcher_homerun",record.getPitcher_homerun()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("볼넷");
        stats.setStat_value(record.getPitcher_bb());
        stats.setStat_std(calculateStatsPitcher("pitcher_bb",record.getPitcher_bb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("고4");
        stats.setStat_value(record.getPitcher_ibb());
        stats.setStat_std(calculateStatsPitcher("pitcher_ibb",record.getPitcher_ibb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("사구");
        stats.setStat_value(record.getPitcher_hbp());
        stats.setStat_std(calculateStatsPitcher("pitcher_hbp",record.getPitcher_hbp()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("삼진");
        stats.setStat_value(record.getPitcher_so());
        stats.setStat_std(calculateStatsPitcher("pitcher_so",record.getPitcher_so()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("보크");
        stats.setStat_value(record.getPitcher_bk());
        stats.setStat_std(calculateStatsPitcher("pitcher_bk",record.getPitcher_bk()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("폭투");
        stats.setStat_value(record.getPitcher_wp());
        stats.setStat_std(calculateStatsPitcher("pitcher_wp",record.getPitcher_wp()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("ERA");
        stats.setStat_value(record.getPitcher_era());
        stats.setStat_std(calculateStatsPitcher("pitcher_era",record.getPitcher_era()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("FIP");
        stats.setStat_value(record.getPitcher_fip());
        stats.setStat_std(calculateStatsPitcher("pitcher_fip",record.getPitcher_fip()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WHIP");
        stats.setStat_value(record.getPitcher_whip());
        stats.setStat_std(calculateStatsPitcher("pitcher_whip",record.getPitcher_whip()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("ERA+");
        stats.setStat_value(record.getPitcher_era_plus());
        stats.setStat_std(calculateStatsPitcher("pitcher_era_plus",record.getPitcher_era_plus()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("FIP+");
        stats.setStat_value(record.getPitcher_fip_plus());
        stats.setStat_std(calculateStatsPitcher("pitcher_fip_plus",record.getPitcher_fip_plus()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WAR");
        stats.setStat_value(record.getPitcher_war());
        stats.setStat_std(calculateStatsPitcher("pitcher_war",record.getPitcher_war()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WPA");
        stats.setStat_value(record.getPitcher_wpa());
        stats.setStat_std(calculateStatsPitcher("pitcher_wpa",record.getPitcher_wpa()));
        statList.add(stats);

        return statList;
    }

    //stat std 계산
    @Override
    public float calculateStatsPitcher(String stat, float record) throws Exception{
        float max = playerDao.getMaxValuePitcher(stat);
        float min = playerDao.getMinValuePitcher(stat);
        
        if(record <0)
        record = Math.abs(min-record);
        
        float std = record/(max-min);
        std = (float) (Math.round(std * 100) / 100.0);
        
        return std;
    }

    @Override
	public List<StatForChart> getPlayerStatsHitter(int num) throws Exception{
        RecordHitter record = playerDao.getPlayerStatsHitter(num);
        List<StatForChart> statList = new ArrayList<StatForChart>();
        StatForChart stats = new StatForChart();
    
        stats.setStat_name("게임 수");
        stats.setStat_value(record.getHitter_gamecnt());
        stats.setStat_std(calculateStatsHitter("hitter_gamecnt",record.getHitter_gamecnt()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("타석");
        stats.setStat_value(record.getHitter_pa());
        stats.setStat_std(calculateStatsHitter("hitter_pa",record.getHitter_pa()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("타수");
        stats.setStat_value(record.getHitter_ab());
        stats.setStat_std(calculateStatsHitter("hitter_ab",record.getHitter_ab()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("득점");
        stats.setStat_value(record.getHitter_score());
        stats.setStat_std(calculateStatsHitter("hitter_score",record.getHitter_score()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("안타");
        stats.setStat_value(record.getHitter_hit());
        stats.setStat_std(calculateStatsHitter("hitter_hit",record.getHitter_hit()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("2타");
        stats.setStat_value(record.getHitter_double());
        stats.setStat_std(calculateStatsHitter("hitter_double",record.getHitter_double()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("3타");
        stats.setStat_value(record.getHitter_triple());
        stats.setStat_std(calculateStatsHitter("hitter_triple",record.getHitter_triple()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("홈런");
        stats.setStat_value(record.getHitter_homerun());
        stats.setStat_std(calculateStatsHitter("hitter_homerun",record.getHitter_homerun()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("루타");
        stats.setStat_value(record.getHitter_tb());
        stats.setStat_std(calculateStatsHitter("hitter_tb",record.getHitter_tb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("타점");
        stats.setStat_value(record.getHitter_rbi());
        stats.setStat_std(calculateStatsHitter("hitter_rbi",record.getHitter_rbi()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("도루");
        stats.setStat_value(record.getHitter_sb());
        stats.setStat_std(calculateStatsHitter("hitter_sb",record.getHitter_sb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("도실");
        stats.setStat_value(record.getHitter_cs());
        stats.setStat_std(calculateStatsHitter("hitter_cs",record.getHitter_cs()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("볼넷");
        stats.setStat_value(record.getHitter_bb());
        stats.setStat_std(calculateStatsHitter("hitter_bb",record.getHitter_bb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("사구");
        stats.setStat_value(record.getHitter_hbp());
        stats.setStat_std(calculateStatsHitter("hitter_hbp",record.getHitter_hbp()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("고4");
        stats.setStat_value(record.getHitter_ibb());
        stats.setStat_std(calculateStatsHitter("hitter_ibb",record.getHitter_ibb()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("삼진");
        stats.setStat_value(record.getHitter_so());
        stats.setStat_std(calculateStatsHitter("hitter_so",record.getHitter_so()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("병살");
        stats.setStat_value(record.getHitter_gdp());
        stats.setStat_std(calculateStatsHitter("hitter_gdp",record.getHitter_gdp()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("희타");
        stats.setStat_value(record.getHitter_sh());
        stats.setStat_std(calculateStatsHitter("hitter_sh",record.getHitter_sh()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("희비");
        stats.setStat_value(record.getHitter_sf());
        stats.setStat_std(calculateStatsHitter("hitter_sf",record.getHitter_sf()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("타율");
        stats.setStat_value(record.getHitter_ba());
        stats.setStat_std(calculateStatsHitter("hitter_ba",record.getHitter_ba()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("출루율");
        stats.setStat_value(record.getHitter_obp());
        stats.setStat_std(calculateStatsHitter("hitter_obp",record.getHitter_obp()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("장타율");
        stats.setStat_value(record.getHitter_slg());
        stats.setStat_std(calculateStatsHitter("hitter_slg",record.getHitter_slg()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("OPS");
        stats.setStat_value(record.getHitter_ops());
        stats.setStat_std(calculateStatsHitter("hitter_ops",record.getHitter_ops()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("wOBA");
        stats.setStat_value(record.getHitter_woba());
        stats.setStat_std(calculateStatsHitter("hitter_woba",record.getHitter_woba()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("wRC+");
        stats.setStat_value(record.getHitter_wrc());
        stats.setStat_std(calculateStatsHitter("hitter_wrc",record.getHitter_wrc()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WAR");
        stats.setStat_value(record.getHitter_war());
        stats.setStat_std(calculateStatsHitter("hitter_war",record.getHitter_war()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WPA");
        stats.setStat_value(record.getHitter_wpa());
        stats.setStat_std(calculateStatsHitter("hitter_wpa",record.getHitter_wpa()));
        statList.add(stats);
       
        return statList;
    }

    @Override
    public float calculateStatsHitter(String stat, float record) throws Exception{
        float max = playerDao.getMaxValueHitter(stat);
        float min = playerDao.getMinValueHitter(stat);

        if(record <0)
        record = Math.abs(min-record);
        
        float std = record/(max-min);
        std = (float) (Math.round(std * 100) / 100.0);
        
        return std;
    }

    @Override
    public List<StatForChart> getPlayerStatsFielder(int num) throws Exception{
        RecordFielder record = playerDao.getPlayerStatsFielder(num);
        List<StatForChart> statList = new ArrayList<StatForChart>();
        StatForChart stats = new StatForChart();
    
        stats.setStat_name("출장");
        stats.setStat_value(record.getFielder_g());
        stats.setStat_std(calculateStatsFielder("fielder_g",record.getFielder_g()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("선발");
        stats.setStat_value(record.getFielder_gs());
        stats.setStat_std(calculateStatsFielder("fielder_gs",record.getFielder_gs()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("이닝");
        stats.setStat_value(record.getFielder_inn());
        stats.setStat_std(calculateStatsFielder("fielder_inn",record.getFielder_inn()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("기회");
        stats.setStat_value(record.getFielder_ch());
        stats.setStat_std(calculateStatsFielder("fielder_ch",record.getFielder_ch()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("자살");
        stats.setStat_value(record.getFielder_po());
        stats.setStat_std(calculateStatsFielder("fielder_po",record.getFielder_po()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("보살");
        stats.setStat_value(record.getFielder_a());
        stats.setStat_std(calculateStatsFielder("fielder_a",record.getFielder_a()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("실책");
        stats.setStat_value(record.getFielder_e());
        stats.setStat_std(calculateStatsFielder("fielder_e",record.getFielder_e()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("수비율");
        stats.setStat_value(record.getFielder_fld());
        stats.setStat_std(calculateStatsFielder("fielder_fld",record.getFielder_fld()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("RF9");
        stats.setStat_value(record.getFielder_rf9());
        stats.setStat_std(calculateStatsFielder("fielder_rf9",record.getFielder_rf9()));
        statList.add(stats);
       
        stats = new StatForChart();
        stats.setStat_name("RNG");
        stats.setStat_value(record.getFielder_rng());
        stats.setStat_std(calculateStatsFielder("fielder_rng",record.getFielder_rng()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("ARM");
        stats.setStat_value(record.getFielder_arm());
        stats.setStat_std(calculateStatsFielder("fielder_arm",record.getFielder_arm()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("CS");
        stats.setStat_value(record.getFielder_cs());
        stats.setStat_std(calculateStatsFielder("fielder_cs",record.getFielder_cs()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("BLK");
        stats.setStat_value(record.getFielder_blk());
        stats.setStat_std(calculateStatsFielder("fielder_blk",record.getFielder_blk()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("E+");
        stats.setStat_value(record.getFielder_e_plus());
        stats.setStat_std(calculateStatsFielder("fielder_e_plus",record.getFielder_e_plus()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("RAA");
        stats.setStat_value(record.getFielder_raa());
        stats.setStat_std(calculateStatsFielder("fielder_raa",record.getFielder_raa()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("/133");
        stats.setStat_value(record.getFielder_133());
        stats.setStat_std(calculateStatsFielder("fielder_133",record.getFielder_133()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("POSADJ");
        stats.setStat_value(record.getFielder_posadj());
        stats.setStat_std(calculateStatsFielder("fielder_posadj",record.getFielder_posadj()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("RAAwithADJ");
        stats.setStat_value(record.getFielder_raawithadj());
        stats.setStat_std(calculateStatsFielder("fielder_raawithadj",record.getFielder_raawithadj()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WAAw/oADJ");
        stats.setStat_value(record.getFielder_waawoadj());
        stats.setStat_std(calculateStatsFielder("fielder_waawoadj",record.getFielder_waawoadj()));
        statList.add(stats);

        stats = new StatForChart();
        stats.setStat_name("WAAwithADJ");
        stats.setStat_value(record.getFielder_waawithadj());
        stats.setStat_std(calculateStatsFielder("fielder_waawithadj",record.getFielder_waawithadj()));
        statList.add(stats);

        return statList;
    }

    @Override
    public float calculateStatsFielder(String stat, float record) throws Exception{
        float max = playerDao.getMaxValueFielder(stat);
        float min = playerDao.getMinValueFielder(stat);

        if(record <0)
        record = Math.abs(min-record);
        
        float std = record/(max-min);
        std = (float) (Math.round(std * 100) / 100.0);
        
        return std;
    }

}
