package com.ssafy.bigdata.dto;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
