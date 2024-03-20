package com.kh.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomJoin {
	
	private int userNo;
	private int chatRoomNo;
	private UserStatus userStatus; // 1 -> 접속중 , 2-> 나감 
}
