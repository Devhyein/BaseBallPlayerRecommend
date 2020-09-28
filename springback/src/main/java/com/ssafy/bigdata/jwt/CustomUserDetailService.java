package com.ssafy.bigdata.jwt;

import lombok.RequiredArgsConstructor;

import com.google.common.base.Optional;
import com.ssafy.bigdata.dto.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(username);
            if(user!=null){
               return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}