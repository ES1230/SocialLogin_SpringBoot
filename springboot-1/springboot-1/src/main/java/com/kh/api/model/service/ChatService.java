package com.kh.api.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.api.model.dao.ChatDao;
import com.kh.api.model.vo.ChatMessage;
import com.kh.api.model.vo.ChatRoom;
import com.kh.api.model.vo.ChatRoomJoin;
import com.kh.api.model.vo.Member;

@Service
public class ChatService {
	
	@Autowired
	private ChatDao dao;

	public List<ChatRoom> selectChatRooms() {
		return dao.selectChatRooms();
	}

	public int openChatRoom(ChatRoom cr) {
		return dao.openChatRoom(cr);
	}

	public Member joinChatRoom(ChatRoomJoin crj) {
		try {
			// ChatRoomJoin테이블에 insert시키는 함수
			dao.joinChatRoom(crj);
		}catch(Exception e) {
			
		}
		return dao.selectUser(crj);
	}

	public List<ChatMessage> selectMessages(int chatRoomNo) {
		return dao.selectMessages(chatRoomNo);
	}

	public void insertChatMessage(ChatMessage chatMessage) {
		dao.insertChatMessage(chatMessage);
	}

	public List<Member> selectUsers(int chatRoomNo) {
		return dao.selectUsers(chatRoomNo);
	}

	public Member exitChatRoom(ChatRoomJoin crj) {
		dao.exitChatRoom(crj);
		return dao.selectUser(crj);
	}

	public Member updateUserStatus(ChatRoomJoin crj) {
		dao.updateUserStatus(crj);
		
		return dao.selectUser(crj);
	}

	public void selectUser(ChatMessage chatMessage) {
		dao.selectUser(chatMessage);
	}

}
