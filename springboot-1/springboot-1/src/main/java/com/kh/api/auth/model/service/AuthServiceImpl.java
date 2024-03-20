package com.kh.api.auth.model.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.api.auth.model.dao.AuthDao;
import com.kh.api.auth.model.dto.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

	private final AuthDao authDao;
	
	@Override
	public User loadUserByUsername(String socialId) throws UsernameNotFoundException {
		// 사용자 인증 프로세스 정의
		// 사용자가 제공한 인증정보를 검증하여 실제 존재하는 회원인지 확인.
		// 존재하만다면 사용자의 아이디, 권한들을 가지고있는 UserDetails객체를 반환해줘야함.
		return authDao.loadUserByUsername( Long.parseLong(socialId) , "kakao" );
	}

}
