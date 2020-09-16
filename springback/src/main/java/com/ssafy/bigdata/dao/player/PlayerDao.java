package com.ssafy.bigdata.dao.player;

import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RecordFielder;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsPitcher;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlayerDao {

	public List<Player> searchPlayerList(String search);

	public String findTeamName(int team_id);

	public String findPlayerPosition(int num);

	public RecordPitcher getPlayerStatsPitcher(int num);

	public RecordHitter getPlayerStatsHitter(int num);

	public RecordFielder getPlayerStatsFielder(int num);
    
}
