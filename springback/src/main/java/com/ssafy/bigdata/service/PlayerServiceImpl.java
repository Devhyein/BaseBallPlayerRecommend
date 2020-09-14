package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Player;
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
    public List<Player> searchPlayerList(String search) {
        try {
            List<Player> playerlist = playerDao.searchPlayerList(search);
            if (playerlist.size()>0){
                for(Player p : playerlist){
                    p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
                }
            }
            System.out.println(playerlist.get(0).toString());
            return playerlist;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
