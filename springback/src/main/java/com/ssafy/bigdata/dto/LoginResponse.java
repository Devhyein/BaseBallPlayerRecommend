package com.ssafy.bigdata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private int id;
    private String email;

    public LoginResponse() {
    }

    public LoginResponse(int id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginResponse [email=" + email + ", id=" + id + "]";
    }

    
}
