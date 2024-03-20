package com.kh.api.auth.model.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kh.api.auth.model.dto.KakaoUserInfoResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class KakaoUserInfo { // 카카오 api를 이용해서 토큰을 전송하여 유저정보를 요청하는 클래스

	// http통신을 위한 객체(의존성 주입)
	private final WebClient webClient;
	
	// 사용자의 정보를 얻을수있는 url주소
	private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
	
	
	// 프론트에서 전달한 accessToken을 활용하여 카카오 api서버로 유저 정보 요청
	public KakaoUserInfoResponse getUserInfo(String accessToken) {
		
		Flux<KakaoUserInfoResponse> response = webClient
				.get()
				.uri(KAKAO_USER_INFO_URI)
				.header("Authorization", "Bearer "+accessToken)
				.retrieve()
				.bodyToFlux(KakaoUserInfoResponse.class);
		
		return response.blockFirst();
	}
	
	
	
	
	
}
