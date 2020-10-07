package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dto.User;

public interface UserService {
     // 유저 정보 가져오기(토큰에서)
     User getUserByToken(String token);

     // 토큰 만들기
     String getTokenByEmail(String email);
}
