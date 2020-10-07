package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dao.user.UserDao;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
