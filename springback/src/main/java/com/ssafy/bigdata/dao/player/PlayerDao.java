package com.ssafy.bigdata.dao.player;

import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RecordFielder;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlayerDao {

	public List<Player> searchPlayerList(String positions, String teams, String searchText);

	public List<Player> searchAllPlayerList(String positions, String teams);

	public Player searchPlayerById(int player_id);

	public String findTeamName(int team_id);

	public String findPlayerPosition(int num);

	public RecordPitcher getPlayerStatsPitcher(int num);

	public RecordHitter getPlayerStatsHitter(int num);

	public RecordFielder getPlayerStatsFielder(int num);

	public float getMaxValuePitcher(String stat);

	public float getMinValuePitcher(String stat);

	public float getMaxValueHitter(String stat);

	public float getMinValueHitter(String stat);

	public float getMaxValueFielder(String stat);

	public float getMinValueFielder(String stat);
    
}
