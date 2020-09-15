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

	public List<Object> getPlayerStacksPitcher(int num);

	public List<Object> getPlayerStacksHitter(int num);

	public ToolsPitcher calculateToolsPitcher(RecordPitcher record);

	public ToolsHitter calculateToolsHitter(RecordHitter hitter, RecordFielder fielder);

    
}
