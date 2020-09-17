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

	public int getAgeWithBirth(String birth);

	public String findPlayerPosition(int num);

	public ToolsPitcher getPlayerToolsPitcher(int num) throws Exception;

	public ToolsHitter getPlayerToolsHitter(int num) throws Exception;

	public ToolsPitcher calculateToolsPitcher(RecordPitcher record) throws Exception;

	public ToolsHitter calculateToolsHitter(RecordHitter hitter, RecordFielder fielder) throws Exception;

	public List<StatForChart> getPlayerStatsPitcher(int num) throws Exception;

	public float calculateStatsPitcher(String stat, float record) throws Exception;

	public List<StatForChart> getPlayerStatsHitter(int num) throws Exception;

	public float calculateStatsHitter(String stat, float record) throws Exception;

	public List<StatForChart> getPlayerStatsFielder(int num) throws Exception;

	public float calculateStatsFielder(String stat, float record) throws Exception;
}
