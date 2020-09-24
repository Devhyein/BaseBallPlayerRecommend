package com.ssafy.bigdata.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.Optional;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(user.get("email"),user.get("name")); // 최초 가입시 USER 로 설정

    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        try {
            User user2 = userRepository.findByEmail(user.get("email"));
            if(user2!=null){
                return jwtTokenProvider.createToken(user2.getEmail());
            } else {
                System.out.println("가입되지 않은 Email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("##U*$Q#)$*#()$*#)(#");
        }
        return null;
    }
}