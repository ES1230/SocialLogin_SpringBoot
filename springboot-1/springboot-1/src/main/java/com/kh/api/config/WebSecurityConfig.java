package com.kh.api.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.kh.api.auth.filter.JwtFilter;
import com.kh.api.auth.model.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final JwtTokenProvider jwtProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		// cors설정
		
		http
		.cors(corsConfig -> corsConfig.configurationSource( new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 오리진설정
				config.setAllowedMethods(Collections.singletonList("*")); // get/post등 허용 메서드 설정
				config.setAllowCredentials(true); // 요청시 인증정보를 포함시킬지 여부. 
				config.setAllowedHeaders(Collections.singletonList("*"));//요청헤더 허용 설정
				config.setMaxAge(3600L); //1시간 요청정보 캐싱시간.
				return config;
			}
		}))
		.csrf( (csrf) -> csrf.disable())
		.sessionManagement(
			sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		)
		//URL별 권한관리
		.authorizeHttpRequests( (authorizeReq) -> 
			// 로그인 및 그외 회원가입등의 rest는 권한없이 이용 가능하도록 설정.
			authorizeReq
			.requestMatchers("/auth/login/**").permitAll() // permitAll() : 사용자가 인증하지 않아도, 권한이 없어도 누구나 사용가능.
			.requestMatchers("/stompServer/**").permitAll() 
			.requestMatchers("/**").hasRole("USER") // 그외 URL은 이용하려면 USER권한이 필요함을 의미. 
			//.requestMatchers("/admin/**").hasRole("ADMIN")
			//.requestMatchers("/user/**").hasRole("USER") 권한별로 이용가능한 url을 설정할수 있음.
			.anyRequest().authenticated() // 인증시에만 이용 가능하도록 설정.
		)
		// addFilterBefore : 스프링시큐리틔 filter 작동전에 먼저 실행시킬 필터를 등록시키는 함수.
		// UsernamePasswordAuthenticationFilter : 사용자가 입력한 id/pw를 통해 사용자를 인증시키는 필터
		// JwtFilter : 클라이언트가 전달한 jwt토큰을 활용하여 사용자를 인증시키는 필터.
		.addFilterBefore( new JwtFilter(jwtProvider) , UsernamePasswordAuthenticationFilter.class )
		;//
		
		return http.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
