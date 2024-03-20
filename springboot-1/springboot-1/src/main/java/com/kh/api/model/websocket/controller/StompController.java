package com.kh.api.model.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.kh.api.model.service.ChatService;
import com.kh.api.model.vo.ChatMessage;
import com.kh.api.model.vo.ChatRoomJoin;
import com.kh.api.model.vo.Member;

@Controller
public class StompController {
	
	@Autowired
	private ChatService service;
	
	private final Logger logger = LoggerFactory.getLogger(StompController.class);
	
	//채팅방에 메세지 추가기능
	// http:~~/api/chat/sendMessage/chatRoomNo/{chatRoomNo}
	@MessageMapping("/sendMessage/chatRoomNo/{chatRoomNo}")
	@SendTo("/chat/chatRoomNo/{chatRoomNo}/message")// 
	public ChatMessage insertChatMessage(
			ChatMessage chatMessage) {
		//1) db에 등록후
		logger.info("chatMessage ?? {}" , chatMessage);
		service.insertChatMessage(chatMessage);
		
		service.selectUser(chatMessage);
		
		//2) 같은방 사용자에게 다시 전달
		return chatMessage;
	}
	
	@MessageMapping("/chatRoom/chatRoomNo/{chatRoomNo}/member/{userNo}/newMember")
	@SendTo("/chat/chatRoomNo/{chatRoomNo}/newMember")
	public Member newMember(@DestinationVariable int userNo , 
			@Payload Member m
			) {
		return m;
	}
	
	@MessageMapping("/chatRoomJoin/{chatRoomNo}/{userNo}/delete")
	@SendTo("/chat/chatRoomNo/{chatRoomNo}/exitMember")
	public int exitMember(
			@DestinationVariable int chatRoomNo, 
			@DestinationVariable int userNo, 
			@RequestBody Member m
			) {
		ChatRoomJoin crj = new ChatRoomJoin();
		crj.setChatRoomNo(chatRoomNo);
		crj.setUserNo(userNo);
		logger.info("12 {}",crj);
		
		service.exitChatRoom(crj);
		
		return userNo;
	}
	
	@MessageMapping("/chatRoomJoin/{chatRoomNo}/member/{userNo}/updateStatus")
	@SendTo("/chat/chatRoomNo/{chatRoomNo}/userStatus")
	public Member updateChatUser(
			@RequestBody ChatRoomJoin crj,
			@DestinationVariable int chatRoomNo ,
			@DestinationVariable int userNo
			) {
		return service.updateUserStatus(crj);
	}
	
	
	
}
