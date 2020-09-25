package com.ssafy.bigdata.controller;

import com.ssafy.bigdata.dao.user.UserDao;
import com.ssafy.bigdata.dto.LoginRequest;
import com.ssafy.bigdata.dto.RestResponse;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class UserController {
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
            String email = userService.login(request);
            User user = userDao.findByEmail(email);
            response.status = true;
            response.msg = "success";
            response.data = user;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
