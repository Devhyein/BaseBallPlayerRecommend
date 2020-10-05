package com.ssafy.bigdata.service;

import java.util.List;

import com.ssafy.bigdata.dao.player.FavoritesDao;
import com.ssafy.bigdata.dto.Favorites;
import com.ssafy.bigdata.dto.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    @Autowired
    private FavoritesDao favoritesDao;

    @Override
    public List<Player> getFavoritesPlayer(int user_id) {
        return favoritesDao.getFavoritesPlayer(user_id);
    }

    @Override
    public int insertFavorites(Favorites favorites) {
        return favoritesDao.insertFavorites(favorites);
    }

    @Override
    public int deleteFavorites(Favorites favorites) {
        return favoritesDao.deleteFavorites(favorites);
    }

    @Override
    public boolean isFavorite(Favorites favorites) {
        return favoritesDao.isFavorite(favorites)>0;
    }
}
