import {useSelector} from 'react-redux';
import MyChat from './MyChat';
import OtherChat from './OtherChat';

export default function Messages({chatMessages}){
    let user = useSelector((state) => state.user);

    return (
        chatMessages.map((chatMessage) => {
            return (
                chatMessage.userNo == user.userNo ? <MyChat chatMessage={chatMessage}/ > :<OtherChat chatMessage={chatMessage}/>
            )            
        })
    )
}