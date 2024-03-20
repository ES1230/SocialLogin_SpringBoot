export default function ChatRoomMembers({chatRoomMembers}){
    return (
        <div className="chat-room-members">
            <h4>참여자 목록</h4>
            <ul className="chat-room-members-ul">
                {
                    chatRoomMembers.map( chatMember => {
                        return (
                            <li key={chatMember.userNo}>
                                <span className={"user-status " +(chatMember.userStatus == 1 ? 'online' : 'offline' )  }></span>{chatMember.nickName}
                            </li>
                        )
                    })
                }
            </ul>
        </div>
    )
}
