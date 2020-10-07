package com.ssafy.bigdata.dao.user;

import com.ssafy.bigdata.dto.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

	User findByEmail(String email);
	Long save(String email, String name);
}
