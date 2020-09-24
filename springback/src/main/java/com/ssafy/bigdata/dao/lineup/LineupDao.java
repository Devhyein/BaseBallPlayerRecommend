package com.ssafy.bigdata.dao.lineup;

import com.ssafy.bigdata.dto.Lineup;

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
     public int insertLineup(Lineup lineup);
    
}
