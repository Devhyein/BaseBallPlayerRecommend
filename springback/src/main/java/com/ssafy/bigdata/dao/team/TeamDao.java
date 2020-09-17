package com.ssafy.bigdata.dao.team;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.Player;

@Mapper
@Repository
public interface TeamDao {
    public Lineup getLineupByTeamId(int num);

	public Lineup getPlayerListByLineup(int lineup);

	public List<Lineup> getLineup();
}
