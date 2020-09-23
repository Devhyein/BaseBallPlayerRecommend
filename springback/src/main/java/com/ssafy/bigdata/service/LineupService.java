package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;

public interface LineupService {
     //라인업리스트 가져오기
     public List<Lineup> getLineupList();

     //라인업 선택시 해당 리스트 가져오기
     public List<Integer> getPlayerListByLineup(int lineup) throws Exception;
 
     //라인업 수정
     public int updateLineup(Lineup lineup);
 
     //라인업 삭제
     public int deleteLineup(int lineup);
 
     //라인업 새로 저장
     public int insertLineup(Lineup lineup);

}
