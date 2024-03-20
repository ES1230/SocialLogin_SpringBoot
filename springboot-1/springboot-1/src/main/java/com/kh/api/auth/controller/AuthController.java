package com.kh.api.auth.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.api.auth.model.dto.User;
import com.kh.api.auth.model.jwt.JwtTokenProvider;
import com.kh.api.auth.model.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final KakaoAuthService service;
	private final JwtTokenProvider provider;
	
	
	@PostMapping("/login/{sns}")
	public ResponseEntity<HashMap<String, Object>> authCheck(
			@PathVariable String sns , 
			@RequestBody HashMap<String ,String> param
			){
		
		User user = service.login(param.get("idToken"));
		
		//1) 클라이언트로부터 전달받은 access_token으로 카카오 api서버에 요청을 보낸후 전달받은 회원의 고유 id값을
		//   통해 우리의 서비스에 회원가입을 했는지 확인. 회원가입 되어있지 않다면 회원가입 처리를 시켜줄 예정.
		HashMap<String , Object> map = new HashMap<>();
		map.put("user", user);
		map.put("jwtToken", provider.createToken(user.getSocialId()));
		
		//map 안에 데이터 ? jwt토큰 담아줄 예정.
		//              + 자주 사용되는 사용자 정보들( 썸네일 , 닉네임, 이메일정보를 함께 저장시켜줄 예정)
		
		
		return ResponseEntity.ok(map);
	}
}
