package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.StatForChart;

public interface PlayerService {

	public List<Player> searchPlayerList(String search);
    
}
