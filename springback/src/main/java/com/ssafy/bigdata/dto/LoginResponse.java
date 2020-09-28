package com.ssafy.bigdata.dto;

public class LoginResponse {
    private int id;
    private String email;
    private String picture;
    private String name;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(int id, String email, String picture, String name, String token) {
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.name = name;
        this.token = token;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse [email=" + email + ", id=" + id + ", name=" + name + ", picture=" + picture + ", token="
                + token + "]";
    }



    
}
