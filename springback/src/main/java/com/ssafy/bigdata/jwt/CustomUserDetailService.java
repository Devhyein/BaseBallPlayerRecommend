package com.ssafy.bigdata.jwt;

import com.ssafy.bigdata.dao.user.UserRepository;
import com.ssafy.bigdata.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(username);
            if(user!=null){
               return null;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}