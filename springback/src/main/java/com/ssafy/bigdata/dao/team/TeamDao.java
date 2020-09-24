package com.ssafy.bigdata.dao.team;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeamDao {
	public Lineup getPlayerListByLineup(int lineup);

	public List<Lineup> getLineup();
}
