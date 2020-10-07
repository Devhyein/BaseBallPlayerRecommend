package com.ssafy.bigdata.service;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dao.lineup.LineupDao;
import com.ssafy.bigdata.dao.team.TeamDao;
import com.ssafy.bigdata.dto.Lineup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineupServiceImpl implements LineupService {

    @Autowired
    private LineupDao lineupDao;

    @Autowired
    private TeamDao teamDao;


    // 라인업리스트 가져오기
    @Override
    public List<Lineup> getLineupList() {
        List<Lineup> lineup = teamDao.getLineup();
        return lineup;
    }

    // 로그인 한 유저의 라인업리스트 & 기본 리스트 가져오기
    @Override
    public List<Lineup> getUserLineupList(int id) {
        List<Lineup> def = lineupDao.getDefaultLineup();
        List<Lineup> lineup = lineupDao.getUserLineup(id);

        for(Lineup line : lineup) {
            def.add(line);
        }

        return def;
    }

    //라인업 선택시 선수리스트 가져오기
    @Override
    public List<Integer> getPlayerListByLineup(int lineup)  throws Exception{
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

    //라인업 수정
    @Override
    public int updateLineup(Lineup lineup) {
        return lineupDao.updateLineup(lineup);
    }
     
    //라인업 삭제
    @Override
    public int deleteLineup(int lineup) {
        return lineupDao.deleteLineup(lineup);
    }

    //라인업 새로 저장
    @Override
    public int insertLineup(String lineup_name, int hitter1, int hitter2, int hitter3, int hitter4, int hitter5,
    int hitter6, int hitter7, int hitter8, int hitter9, int pitcher, int id) {
        try {
            return lineupDao.insertLineup(lineup_name, hitter1, hitter2, hitter3, hitter4, hitter5, hitter6, hitter7, hitter8, hitter9, pitcher, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int modifyLineupName(Lineup lineup) {
        return lineupDao.modifyLineupName(lineup);
    }
}
