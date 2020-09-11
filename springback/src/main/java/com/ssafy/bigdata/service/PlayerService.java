package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.StatForChart;

import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

	List<StatForChart> searchPlayerList(String search);
    
}
