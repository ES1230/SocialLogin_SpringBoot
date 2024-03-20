package com.kh.api.auth.model.dto;

import lombok.Getter;
import lombok.ToString;

// 카카오에서 반환시키는 응답데이터를 저장할 dto
@Getter @ToString
public class KakaoUserInfoResponse {
	
	private Long id;
	private Boolean has_signed_up;
	private String connected_at;
	private String synched_at;
	
	private KakaoProperties properties;
	private KakaoAccount kakao_account;	
	
	
	// 그외 카카오 응답관련 데이터를 저장할 dto클래스들은 모두 innerclass로 생성하기
	@Getter
	public static class KakaoProperties {
		private String nickname;
		private String profile_image;
		private String thumbnail_image;
	}
	
	@Getter
	public static class KakaoAccount {
		private Boolean profile_needs_agreement; // 사용자 동의시 프로필 정보 제공 기능
		private KaokaoProfile profile;
		private Boolean email_needs_agreement; 
		private String email;// 비즈앱 전환해야만 얻어 올 수 있음.
	}
	
	@Getter
	public static class KaokaoProfile {
		private String nickname;
		private String thumbnail_image_url;
		private String profile_image_url;
		private Boolean is_default_image;
	}
	
}
