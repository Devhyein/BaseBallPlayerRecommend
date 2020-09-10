package com.ssafy.bigdata.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

	List<String> searchPlayerList();
    
}
