package com.kh.api.model.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


import lombok.RequiredArgsConstructor;

/* 
 * Stomp
 * - MessageBroker방식 처리
 * - publih 발행 / subscribe 구독 패턴
 *  - 특정 url을 '구독' 하는 사용자들에게 메세지를 '발행'해줌.
 * */

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer{
	
	/* 
	 * 클라이언트에서 웹소켓 서버로 연결요청을 보낼 endPoint설정
	 * 
	 * http://localhost:8084/stompServer로 요청시 웹소켓 객체 생성.
	 * */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
		.addEndpoint("/stompServer")
		.setAllowedOrigins("http://localhost:3000")		
		.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		// /chat으로 시작하는 url이 발행 가능하도록 설정.
		registry
		.enableSimpleBroker("/chat");
		
		registry
		.setApplicationDestinationPrefixes("/chat"); // 구독 url에 /chat을 자동으로붙임
		
	}
	
	
}






