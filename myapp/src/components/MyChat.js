export default function MyChat({chatMessage}){
    return(
        <li className="myChat">
            <span className="chatDate">{chatMessage.createDate}</span>
            <p className="chat">{chatMessage.message}</p>
        </li>
    )
}