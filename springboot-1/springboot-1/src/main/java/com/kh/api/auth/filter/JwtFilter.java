package com.kh.api.auth.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.kh.api.auth.model.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean{
	
	//토큰발행
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 클라이언트가 전달한 요청 정보에서 토큰을 추출하여,
		// 인증된 사용자인지 확인후 인증정보를 스프링 시큐리티에 저장.
		String token = jwtTokenProvider.resolveToken( (HttpServletRequest) request);
		
		// 토큰 유효성 검사실행		
		if(token != null && jwtTokenProvider.validateToken(token)) {
			// 사용할수 있는 토큰이라면, 사용자 인증정보를 조회 후 SecurityContext에 저장
			// SecurityContext? 인증된 사용자 정보를 저장하는 클래스. 스프링시큐리티에 의해 관리된다.
			//                  인증정보 저장을 한 후 , 필터체인에서 사용자가 인증되었는지, 권한이 있는지 조회할때 확인.
			Authentication auth = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		//다음 필터 작동
		chain.doFilter(request, response);
	}
	
	
	
	
	
	
	

}
