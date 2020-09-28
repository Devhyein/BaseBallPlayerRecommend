package com.ssafy.bigdata.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.*;

import com.ssafy.bigdata.dao.user.UserDao;
import com.ssafy.bigdata.dto.LoginRequest;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/spring")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public Object googleLogin(@RequestBody final LoginRequest request) {
        final RestResponse response = new RestResponse();
        response.status = false;
        response.msg = "failed";
        response.data = null;

        try {
            // 이미 존재하는 메일인가?
            User user = userRepository.findByEmail(request.getEmail());
            if(user==null) {
                // 회원가입
                userRepository.save(request.getEmail(),request.getName());
                user = userRepository.findByEmail(request.getEmail());
            }
            // 토큰 발급
            String token = jwtTokenProvider.createToken(request.getEmail());
            response.status = true;
            response.msg = "success";
            response.data = token;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @ApiOperation(value = "임시로그인")
    @PostMapping("/login/temp")
    public Object googleLogin_temp() {
        final RestResponse response = new RestResponse();
        response.status = false;
        response.msg = "failed";
        response.data = null;
        try {
            String token = jwtTokenProvider.createToken("test@test.com");
            response.status = true;
            response.msg = "success";
            response.data = token;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
