package com.kh.api.auth.model.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kh.api.model.vo.Member;

public class User extends Member implements UserDetails{
		
	// 복수개의 권한을 저장할 필드
	// SimpleGrantedAuthority문자열 권한을 처리할수 있는 클래스(ROLE_USER ,ROLE_ADMIN ,...)
	private List<SimpleGrantedAuthority> authorities;
	
	// 복수개의 권한을 반환해주는 함수. (db에 저장된 사용자의 권한을 반환할때 사용)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // 사용자의 계정이 만료되었는지 확인
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override // 계정 잠금상태인지 확인.
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override // 비밀번호가 만료되었느지 확인
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
