package com.kh.api.auth.model.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.api.auth.model.dto.User;
import com.kh.api.model.vo.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AuthDao {
	
	private final SqlSessionTemplate session;
	
	public User loadUserByUsername(long socialId, String socialType) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("socialId", socialId);
		param.put("socialType", socialType);

		return session.selectOne("auth.loadUserByUsername" , param);
		
		
	}

	public User insertUserBySocialLogin(Member m) {
		//1) Member 테이블추가 
		session.insert("auth.insertUserBySocialLogin" , m);
		//2) Member_Social테이블에 추가
		session.insert("auth.insertUserSocialBySocialLogin" , m);
		//3) Authroity테이블에 추가
		session.insert("auth.insertUserAuthorityBySocialLogin" , m);
		//4) loadUserByUsername함수호출하여 반환값 전달.
		return this.loadUserByUsername( Long.parseLong(m.getSocialId()) , m.getSocialType());
	}
	
}
