package com.ssafy.bigdata.service;

import com.ssafy.bigdata.dao.user.UserDao;
import com.ssafy.bigdata.dto.LoginRequest;
import com.ssafy.bigdata.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userdao;
    @Autowired
    public void setUserdao(UserDao userdao) {
        this.userdao = userdao;
    }

    @Override
    public String login(LoginRequest request) {
        // 이미 존재하는 메일인가?
        String email = null;
        try {
            User user = userdao.findByEmail(request.getEmail());
            if(user==null) {
                // 아니면 회원가입
                try {
                    User newUser = new User(request.getEmail(), request.getName(), request.getPicture());
                    userdao.save(newUser);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            // 이메일 반환
            email = userdao.findByEmail(request.getEmail()).getEmail();
            

        } catch(Exception e) {
            e.printStackTrace();
        }


        return email;
    }
    
}
