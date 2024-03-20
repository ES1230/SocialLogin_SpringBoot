package com.kh.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.api.model.service.ChatService;
import com.kh.api.model.vo.ChatMessage;
import com.kh.api.model.vo.ChatRoom;
import com.kh.api.model.vo.ChatRoomJoin;
import com.kh.api.model.vo.Member;

@RestController
public class ChatController {
	
	@Autowired
	private ChatService service;
	
	@GetMapping("/chatRoomList")
	public List<ChatRoom> selectChatRooms(){
		return service.selectChatRooms();
	}
	
	@PostMapping("/openChatRoom")
	public int openChatRoom(@RequestBody ChatRoom cr) {
		return service.openChatRoom(cr);
	}
	
	@PostMapping("/joinChatRoom")
	public Member joinChatRoom(@RequestBody ChatRoomJoin crj) {
		System.out.println(crj);
		return service.joinChatRoom(crj);
	}
	
	@GetMapping("/chatMessage/chatRoomNo/{chatRoomNo}")
	public List<ChatMessage> selectMessages(@PathVariable int chatRoomNo){
		return service.selectMessages(chatRoomNo);
	}
	
	@GetMapping("/chatRoomJoin/chatRoomNo/{chatRoomNo}")
	public List<Member> selectUsers(@PathVariable int chatRoomNo){
		return service.selectUsers(chatRoomNo);
	}
	

}
