package com.ssafy.bigdata.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.jwt.*;

import java.util.HashMap;

import com.ssafy.bigdata.dto.LoginRequest;
import com.ssafy.bigdata.dto.LoginResponse;
import com.ssafy.bigdata.dto.RestResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class UserController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

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
            LoginResponse res = new LoginResponse(user.getUser_id(), user.getEmail(), user.getPicture(), user.getName(), token);

            HashMap<String, Object> data = new HashMap<>();
            data.put("userInfo", res);
            data.put("token", token);

            response.status = true;
            response.msg = "success";
            response.data = data;
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
            User user = userRepository.findByEmail("test@test.com");
            LoginResponse res = new LoginResponse(user.getUser_id(), user.getEmail(), user.getPicture(), user.getName(), token);
            
            HashMap<String, Object> data = new HashMap<>();
            data.put("userInfo", res);
            data.put("token", token);
            
            response.status = true;
            response.msg = "success";
            response.data = data;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
