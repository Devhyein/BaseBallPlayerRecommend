package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.StatForChart;

public interface TeamService {

	public List<StatForChart> analyzeStat(int num);
    
}
