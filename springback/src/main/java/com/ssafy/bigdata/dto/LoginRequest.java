package com.ssafy.bigdata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String name;
    private String picture;

    public LoginRequest() {
    }

    public LoginRequest(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "LoginRequest [email=" + email + ", name=" + name + ", picture=" + picture + "]";
    }

    
}
