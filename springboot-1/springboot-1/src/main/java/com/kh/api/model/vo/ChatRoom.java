package com.kh.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
	
	private int chatRoomNo;
	private String title;
	private String status;
	private int userNo;
	
	private String nickName;
	private int cnt;
}
