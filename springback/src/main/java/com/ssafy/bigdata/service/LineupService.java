package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dto.Lineup;
import com.ssafy.bigdata.dto.User;

public interface LineupService {
     //라인업리스트 가져오기
     public List<Lineup> getLineupList();

     //라인업 선택시 해당 리스트 가져오기
     public List<Integer> getPlayerListByLineup(int lineup) throws Exception;
 
     //라인업 수정
     public int updateLineup(Lineup lineup);
 
     //라인업 삭제
     public int deleteLineup(int lineup);

     // 로그인 한 유저의 라인업 가져오기
	public List<Lineup> getUserLineupList(int id);
     
     //라인업 새로 저장
	public int insertLineup(String lineup_name, int hitter1, int hitter2, int hitter3, int hitter4, int hitter5,
			int hitter6, int hitter7, int hitter8, int hitter9, int pitcher, int id);

}
