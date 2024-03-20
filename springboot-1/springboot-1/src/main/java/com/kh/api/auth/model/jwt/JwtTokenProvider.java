package com.kh.api.auth.model.jwt;

import java.util.Base64;
import java.util.Date;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.kh.api.auth.model.dto.User;
import com.kh.api.auth.model.service.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

//토큰 발행 클래스
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	@Value("${jwt.secret}") // 토큰을 암호화 시킬때 사용할 key값.
	private String secretKey;
	
	private final AuthService authService; // 사용자 인증시 필요한 load..메서드가 정의된 객체
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	// JWT토큰 생성
	public String createToken(String socialId) {
		//JWT에 저장할 데이터
		Claims claims = Jwts.claims().setSubject(socialId);
		
		Date now = new Date();
		
		return Jwts
				.builder()
				.setClaims(claims) // 저장할 데이터
				.setIssuedAt(now) // 토큰 발행 시간
				.setExpiration(new Date(now.getTime()+ (30 * 60 * 1000L)))// 토큰 유효시간 (30분)
				.signWith(SignatureAlgorithm.HS256, secretKey)// 암호화시킬 알고리즘 선택 , secrect값
				.compact();
	}
	
	// 인증정보 조회
	/* 
	 * Authentication ? 인증된 사용자의 정보가 담겨있는 객체
	 * - Principal : 인증된 사용자정보.
	 * - Credentails : 인증에 필요한 비밀번호가 담겨있는 객체(내부 인증시 필요하며 보호되고있음)
	 * - Authorities : 인증된 사용자가 가진 권한
	 * */
	public Authentication getAuthentication(String token) {
		
		String socialId = Jwts
				.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		
		User user = (User) authService.loadUserByUsername(socialId);
		return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
	}
	
	// 토큰 유효성, 만료일자 확인
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims  = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			
			return !claims.getBody().getExpiration().before(new Date()); // 만료시간이 지난경우
		
		}catch(Exception e) { // 토큰자체를 못 꺼내는 경우.
			return false;
		}
	}
	
	// 클라이언트가 전달한 token값 가져오기
	public String resolveToken(HttpServletRequest req) {
		String accessToken = req.getHeader("Authorization");
		
		return accessToken;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
