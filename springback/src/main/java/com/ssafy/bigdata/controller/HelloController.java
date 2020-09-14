package com.ssafy.bigdata.controller;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.bigdata.dto.RestResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/spring")
public class HelloController {
    
    @ApiOperation(value = "테스트에여")
    @GetMapping("/hello")
    public Object helloWorld() {
        final RestResponse response = new RestResponse();
        
        List<String> list = new ArrayList<String>();
        list.add("안녕");
        list.add("하세오");

        response.status = true;
        response.msg = "안녕하세요!";
        response.data = list;

        return response;
    }

    @ApiOperation(value = "예외 테스트")
    @GetMapping("/exception")
    public Object exceptionTest() throws Exception {
        final RestResponse response = new RestResponse();

        response.status = true;
        response.msg = "테스트에요!!";

        throw new Exception("Exception Test");
    }
}