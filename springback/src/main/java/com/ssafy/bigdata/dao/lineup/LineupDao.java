package com.ssafy.bigdata.dao.lineup;

import java.util.HashMap;
import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LineupDao {

     //라인업 수정
     public int updateLineup(Lineup lineup);
 
     //라인업 삭제
     public int deleteLineup(int lineup);
 
     //라인업 새로 저장
     public int insertLineup(String lineup_name, int hitter1, int hitter2, int hitter3, int hitter4, int hitter5, int hitter6, int hitter7, int hitter8 , int hitter9 ,int pitcher, int user_id);

	public List<Lineup> getUserLineup(int id);

	public List<Lineup> getDefaultLineup();

}
