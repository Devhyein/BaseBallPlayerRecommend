package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dao.team.TeamDao;
import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.StatForChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {
    private TeamDao teamDao;

    @Autowired
    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    @Override
    public List<StatForChart> analyzeStat(int num) {
            
        // 1. 이 팀의 최신 라인업 불러오기
        try {            
            Lineup lineup = teamDao.getLineupByTeamId(num);
            System.out.println(lineup.toString());
            
            // 2. 라인업의 플레이어들을 검색해서 record기록 가져오기

            // 3. record 표준화

            // 4. 팀 전체의 평균 스탯 분석

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }


        return null;
    }

        

}
