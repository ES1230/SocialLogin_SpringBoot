export default function OtherChat({chatMessage}){
    return(
        <li>
            <div>
                <img src={chatMessage.profile} style={{width:'30px' , borderRadius: '50px'}}/>
                <b>{chatMessage.nickName}</b>
            </div>
            <p className="chat">{chatMessage.message}</p>
            <span className="chatDate">{chatMessage.createDate}</span>
        </li>
    )
}