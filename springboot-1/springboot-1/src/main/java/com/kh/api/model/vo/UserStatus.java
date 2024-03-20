package com.kh.api.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
	@JsonProperty("1")
	online(1)
	,
	@JsonProperty("2")
	offline(2);
	
	private int userStatus;
	
	UserStatus(int userStatus){
		this.userStatus = userStatus;
	}
	
	@JsonValue
	public String getUserStatus() {
		return String.valueOf(userStatus);
	}
	
	public static UserStatus getValueOfUserStatus(int userStatus) {
		UserStatus [] list = UserStatus.values();
		for(UserStatus us : list) {
			if(us.userStatus == userStatus) {
				return us;
			}
		}
		return null;
	}
	
}
