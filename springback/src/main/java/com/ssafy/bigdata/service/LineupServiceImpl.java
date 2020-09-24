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
    public int insertLineup(Lineup lineup) {
        return lineupDao.insertLineup(lineup);
    }

}
