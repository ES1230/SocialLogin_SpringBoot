package com.kh.api.config;

import java.sql.Connection;
import java.time.Duration;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

// WebClient -> 비동기적인 http통신을 할수 있는 객체.
@Configuration
public class WebClientConfig {
	
	@Bean
	public ReactorResourceFactory resourceFactory() {
		ReactorResourceFactory factory = new ReactorResourceFactory();
		factory.setUseGlobalResources(false); // 공유자원 사용안함 설정
		return factory;
	}
	
	@Bean
	public WebClient webClient() {
		// 설정정보를 저장시킬 객체
		Function<HttpClient , HttpClient> mapper = client -> HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS , 10000) // http 커넥션 타임아웃설정
				.doOnConnected( connection -> connection
						.addHandlerLast( new ReadTimeoutHandler(10))  // 읽기/쓰기 타이아웃설정
						.addHandlerLast(new WriteTimeoutHandler(10)))
				.responseTimeout(Duration.ofSeconds(1)); // 응답 타임아웃 설정
				
		return WebClient
				.builder()
				.clientConnector( new ReactorClientHttpConnector(resourceFactory() , mapper))
				.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
