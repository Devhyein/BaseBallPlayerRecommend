package com.ssafy.bigdata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int user_id;
    private String email;
    private String name;
    private String picture;
    private String team_id;

    public User() {
    }

    public User(int user_id, String email, String name, String picture, String team_id) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.team_id = team_id;
    }

    public User(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", name=" + name + ", picture=" + picture + ", team_id=" + team_id
                + ", user_id=" + user_id + "]";
    }

    
    
}
