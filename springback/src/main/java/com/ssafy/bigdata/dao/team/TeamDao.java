package com.ssafy.bigdata.dao.team;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


import com.ssafy.bigdata.dto.Lineup;

@Mapper
@Repository
public interface TeamDao {
    public Lineup getLineupByTeamId(int num);
}
