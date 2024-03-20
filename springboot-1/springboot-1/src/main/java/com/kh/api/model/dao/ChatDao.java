package com.kh.api.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.api.model.vo.ChatMessage;
import com.kh.api.model.vo.ChatRoom;
import com.kh.api.model.vo.ChatRoomJoin;
import com.kh.api.model.vo.Member;

@Repository
public class ChatDao {
	
	@Autowired
	private SqlSessionTemplate session;

	public List<ChatRoom> selectChatRooms() {
		return session.selectList("chat.selectChatRooms");
	}

	public int openChatRoom(ChatRoom cr) {
		session.insert("chat.openChatRoom", cr);
		return cr.getChatRoomNo();
	}

	public void joinChatRoom(ChatRoomJoin crj) {
		session.insert("chat.joinChatRoom", crj);
	}

	public Member selectUser(ChatRoomJoin crj) {
		return session.selectOne("chat.selectUser", crj);
	}

	public List<ChatMessage> selectMessages(int chatRoomNo) {
		return session.selectList("chat.selectMessages" , chatRoomNo);
	}

	public void insertChatMessage(ChatMessage chatMessage) {
		session.insert("chat.insertChatMessage", chatMessage);
	}

	public List<Member> selectUsers(int chatRoomNo) {
		return session.selectList("chat.selectUsers", chatRoomNo);
	}

	public void exitChatRoom(ChatRoomJoin crj) {
		session.delete("chat.exitChatRoom" , crj);
	}

	public void updateUserStatus(ChatRoomJoin crj) {
		session.update("chat.updateUserStatus" , crj);
	}

	public void selectUser(ChatMessage chatMessage) {
		Member m = session.selectOne("chat.selectChatUser" , chatMessage);
		
		chatMessage.setProfile(m.getProfile());
		chatMessage.setNickName(m.getNickName());
	}

}
