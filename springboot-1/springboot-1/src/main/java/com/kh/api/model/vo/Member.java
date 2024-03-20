package com.kh.api.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Builder
public class Member {
	
	private String socialId;	
	private String socialType;
	private int userNo;
	private String email;
	private String nickName;
	private String userPwd;
	private String profile;
	private String status;
	private String enrollDate;
	private String modifyDate;
	
	private UserStatus userStatus;
}
