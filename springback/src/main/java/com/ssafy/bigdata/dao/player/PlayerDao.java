package com.ssafy.bigdata.dao.player;

import java.util.List;

import com.ssafy.bigdata.dto.StatForChart;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PlayerDao {

	public List<StatForChart> searchPlayerList(String search);
    
}
