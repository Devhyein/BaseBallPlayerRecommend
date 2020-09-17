package com.ssafy.bigdata.service;

import java.util.List;
import java.util.Map;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.TeamStat;

public interface TeamService {

	public TeamStat analyzeStat(int num) throws Exception;

	public List<Integer> getPlayerListByLineup(int lineup) throws Exception;

	public TeamStat analyzeStatByPlayerList(List<Integer> playerList);

	public List<Lineup> getLineupList();
    
}
