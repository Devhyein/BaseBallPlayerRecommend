package com.ssafy.bigdata.dao.player;

import java.util.List;

import com.ssafy.bigdata.dto.Favorites;
import com.ssafy.bigdata.dto.Player;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FavoritesDao {

    // 사용자가 즐겨찾기 해놓은 플레이어 리스트 가져오기
    public List<Player> getFavoritesPlayer(int user_id);

    // 사용자가 선수를 즐겨찾기에 추가
    public int insertFavorites(Favorites favorites);

    // 사용자가 선수를 즐겨찾기에서 해제
    public int deleteFavorites(Favorites favorites);

    // 즐겨찾기 여부 확인
    public int isFavorite(Favorites favorites);
    
    
}
