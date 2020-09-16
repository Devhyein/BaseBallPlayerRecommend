package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RecordFielder;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.StatForChart;
import com.ssafy.bigdata.dto.ToolsHitter;
import com.ssafy.bigdata.dto.ToolsPitcher;

public interface PlayerService {

	public List<Player> searchPlayerList(String search);

	public String findPlayerPosition(int num);

	public ToolsPitcher getPlayerStatsPitcher(int num) throws Exception;

	public ToolsHitter getPlayerStatsHitter(int num) throws Exception;

	public ToolsPitcher calculateToolsPitcher(RecordPitcher record) throws Exception;

	public ToolsHitter calculateToolsHitter(RecordHitter hitter, RecordFielder fielder) throws Exception;

    
}
