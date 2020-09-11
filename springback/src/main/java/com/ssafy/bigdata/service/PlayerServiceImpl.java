package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.StatForChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerDao playerDao;

    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public List<StatForChart> searchPlayerList(String search) {
        try {
            return playerDao.searchPlayerList(search);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
