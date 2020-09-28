package com.ssafy.bigdata.dto;

import lombok.Builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Builder
@Entity
public class User implements UserDetails {

    private int user_id;
    private String email;
    private String name;
    private String picture;
    private String token;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", name=" + name + ", picture=" + picture + ", user_id=" + user_id + "token="+token+"]";
    }

    public User() {
    }

    public User(int user_id, String email, String name, String picture) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public User(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public User(int user_id, String email, String name, String picture, String token) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.token = token;
    }
    

}
