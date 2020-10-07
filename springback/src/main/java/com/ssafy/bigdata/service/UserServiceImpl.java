package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dao.user.UserDao;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByToken(String token) {
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return userDao.findByEmail(jwtTokenProvider.getUserPk(token));
        } else {
            return null;
        }
    }

    @Override
    public String getTokenByEmail(String email) {
        return jwtTokenProvider.createToken(email);
    }
    
}