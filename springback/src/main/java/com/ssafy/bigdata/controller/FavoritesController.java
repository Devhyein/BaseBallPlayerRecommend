package com.ssafy.bigdata.controller;

import java.util.List;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dto.Favorites;
import com.ssafy.bigdata.dto.Player;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.service.FavoritesService;
import com.ssafy.bigdata.service.PlayerService;
import com.ssafy.bigdata.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private PlayerService playerService;

    @ApiOperation(value = "즐겨찾기 선수 가져오기")
    @GetMapping("/favList")
    public Object getFavoritesPlayer(@RequestHeader final HttpHeaders header) {
        final RestResponse response = new RestResponse();

        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "Token Failed";
            response.data = null;
            return response;
        }

        List<Player> res = favoritesService.getFavoritesPlayer(user.getUser_id());

        for(Player p : res){
            p.setPlayer_team(playerDao.findTeamName(p.getTeam_id()));
            p.setPosition(playerDao.findPlayerPosition(p.getPlayer_id())); 
            p.setPlayer_age(playerService.getAgeWithBirth(p.getPlayer_birth()));
        }

        response.data = res;
        response.msg = "favorite player list";
        response.status = true;

        return response;

    }

    @ApiOperation(value = "즐겨찾기 선수 삭제")
    @DeleteMapping("/favorites/delete")
    public Object deleteFavorites(@RequestHeader final HttpHeaders header,  @RequestParam int player_id ) {
        final RestResponse response = new RestResponse();
        
        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "Token Failed";
            response.data = null;
            return response;
        }
                
        Favorites favorites = new Favorites();
        favorites.setPlayer_id(player_id);
        favorites.setUser_id(user.getUser_id());

        int res = favoritesService.deleteFavorites(favorites);
       
        if(res!=0){
            response.status = true;
            response.msg = "success";
        }else{
            response.status = false;
            response.msg = "fail to delete favorites";
        }

        return response;

    }

    @ApiOperation(value = "즐겨찾기 선수 추가")
    @PostMapping("/favorites/delete")
    public Object insertFavorites(@RequestHeader final HttpHeaders header, @RequestBody Favorites favorites) {
        final RestResponse response = new RestResponse();
       
        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "Token Failed";
            response.data = null;
            return response;
        }

        int res = favoritesService.insertFavorites(favorites);
        if(res!=0){
            response.status = true;
            response.msg = "success";
        }else{
            response.status = false;
            response.msg = "fail to insert favorites";
        }

        return response;

    }

    @ApiOperation(value = "즐겨찾기 여부")
    @GetMapping("/favorites/check")
    public Object isFavorite(@RequestHeader final HttpHeaders header, @RequestParam int player_id) {
        final RestResponse response = new RestResponse();
        
        User user = userService.getUserByToken(header.get("token").get(0));
     
        if (user == null) {
            System.out.println("토큰이 없거나, 유효하지 않은 토큰입니다.");
            response.status = false;
            response.msg = "Token Failed";
            response.data = null;
            return response;
        }

        Favorites favorites = new Favorites();
        favorites.setPlayer_id(player_id);
        favorites.setUser_id(user.getUser_id());

        boolean res = favoritesService.isFavorite(favorites);

        if(res == true){
            response.status = true;
            response.msg = "favorites";
            response.data = res;
        }else{
            response.status = false;
            response.msg = "not favorites";
            response.data = res;
        }
        return response;
    }
    
}
