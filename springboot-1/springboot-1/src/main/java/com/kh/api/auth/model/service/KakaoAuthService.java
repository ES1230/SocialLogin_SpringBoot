package com.kh.api.auth.model.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.api.auth.model.dao.AuthDao;
import com.kh.api.auth.model.dto.KakaoUserInfoResponse;
import com.kh.api.auth.model.dto.User;
import com.kh.api.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
	
	private final KakaoUserInfo kakaoUserInfo;
	private final AuthDao dao;
	private final PasswordEncoder encoder;

	public User login(String token) {
		
		// 카카오 서버에 데이터 반환 요청 넣기
		KakaoUserInfoResponse userInfo =  kakaoUserInfo.getUserInfo(token);
		
		// 현재 서비스(어플리케이션)에 db상에 사용자 정보가 있는지 조회 -> id + kakao
		Long socialId = userInfo.getId();//+kakao 
		
		User user = dao.loadUserByUsername(socialId , "kakao");// 첫 로그인이라면 무조건 null
		
		// 회원가입이 안된 상태.
		if(user == null) {
			//Member, Mebmer_SOCIAL, AUTHROITY테이블들에 데이터 추가.
			String nickName = userInfo.getProperties().getNickname();
			//랜덤 비밀번호
			String password = UUID.randomUUID().toString();
			String encodedPassword = encoder.encode(password);
			String profile = userInfo.getProperties().getProfile_image();
			String email = userInfo.getKakao_account().getEmail();
			
			Member m = Member
					 .builder()
					 .email(email)
					 .nickName(nickName)
					 .userPwd(encodedPassword)
					 .profile(profile)
					 .socialId(String.valueOf(socialId))
					 .socialType("kakao")
					 .build();
			
			user = dao.insertUserBySocialLogin(m);
		}
		
		return user;
	}

}
