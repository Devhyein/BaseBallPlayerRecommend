package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dto.LoginRequest;
import com.ssafy.bigdata.dto.LoginResponse;

public interface UserService {

	String login(LoginRequest request);
    
}
