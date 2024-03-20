import React, { useEffect, useState , useRef } from "react"
import axios from 'axios';
import {useSelector} from 'react-redux';
import {useParams , useNavigate} from 'react-router-dom';
import Messages from "../components/Messages";
import SockJs from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import ChatRoomMembers from "../components/ChatRoomMembers";

export default function ChattingRoom (){

    let user = useSelector((state) => state.user);
    let [chatMessages , setChatMessages] = useState([]);
    const [webSocket , setWebSocket] = useState(null);
    let [message , setMessage] = useState('');
    let [chatRoomMembers , setChatRoomMembers] = useState([]);
    const bottomRef = useRef(null);
    const textareaRef = useRef(null);
    const navi = useNavigate();

    const { chatRoomNo } = useParams();
    useEffect( ()=> {
        
        // 웹소켓 연결할수 있는 함수
        //const createWebsocket = () => new SockJs("http://localhost:3000/api/stompServer");
        const createWebsocket = () => new SockJs("http://localhost:8084/api/stompServer");

        // 웹소켓 객체 생성은 Stmop가 주관할 예정
        const stompClient = Stomp.over(createWebsocket);

        // jwt전달을 위한 해더설정 추가
        

        stompClient.connect({}, (frame) => {
            console.log(frame);
            //stomp같은경우 웹서버와 통신을할때 frame단위로 동작을함. frame를 메시지를 교환하는 틀이라고 생각할것.

            //구독 url 설정.
            // 내가 구독한 url로 데이터가 전달이되면 실시간으로 감지하고 콜백함수를 실행함.
            stompClient.subscribe(`/chat/chatRoomNo/${chatRoomNo}/message` , async (frame) => {
                console.log(frame.body); // JSON형태의 데이터. 가공처리는 직접해줘야함.
                let jsonMessage = frame.body;
                let parsedMessage = await JSON.parse(jsonMessage);
                // 의존성배열이 비어있는 useEffect내부에서 state값은 항상 "초기 랜더링시의 값"만을 가지고있다.
                setChatMessages((preState) => [...preState, parsedMessage] )
            } ,{} );
            //채팅방에 누가 입장했을때
            stompClient.subscribe(`/chat/chatRoomNo/${chatRoomNo}/newMember`, async (frame) => {
                let jsonMember = frame.body;
                let user = await JSON.parse(jsonMember);
                setChatRoomMembers((preState) => {
                    let fillterlist = preState.filter( u => u.userNo !== user.userNo);
                    return [...fillterlist , user];
                })  
            });
            //채팅방에서 누가 퇴장시 /chat/chatRoomNo/{chatRoomNo}/exitMember
            // /chat/chatRoomNo/{chatRoomNo}/exitMember
            stompClient.subscribe(`/chat/chatRoomNo/${chatRoomNo}/exitMember` , async (frame) => {
                //console.log(frame.body)
                let user = await JSON.parse(frame.body);
                setChatRoomMembers((state) => state.filter((chatUser) => chatUser.userNo != user))
            });

            //채팅방에 접속중인 다른 사용자의 상태값(온라인 ,오프라인)이 바뀔때
            stompClient.subscribe(`/chat/chatRoomNo/${chatRoomNo}/userStatus` , async (frame) => {
                let user = await JSON.parse(frame.body);
                console.log(user);
                setChatRoomMembers( (state) => {
                    let filterUser = state.filter( (chatUser) => chatUser.userNo != user.userNo);
                    return [...filterUser , user];
                }  )

            });
            let chatRoomJoin = {
                userNo : user.userNo ,
                chatRoomNo,
                userStatus : "1"
            }
             //1) CHAT_ROOM_JOIN테이블에 참여자정보 추가.
            axios
            .post("/api/joinChatRoom" , chatRoomJoin )
            .then((res) => {
                console.log('참여완료');
                //3) 채팅방 참여자 정보 조회
                axios
                .get("/api/chatRoomJoin/chatRoomNo/"+chatRoomNo)
                .then( (list) => {
                    setChatRoomMembers(list.data);
                })
                //현재 채팅방을 구독중인 다른 사용자들에게 뉴비가 왔음을 알리는 코드
                stompClient.send("/chat/chatRoom/chatRoomNo/"+chatRoomNo+"/member/"+user.userNo+"/newMember",{}, JSON.stringify(res.data));
            })
            //2) 채팅방 메세지 목록 가져오기
            axios
            .get("/api/chatMessage/chatRoomNo/"+chatRoomNo)
            .then( (list) => {
            setChatMessages(list.data);  
            }).catch(err => console.log(err))

            setTimeout(()=>{
                scrollToBottom();
            },100);
            
            stompClient.send("/chat/chatRoomJoin/"+chatRoomNo+"/member/"+user.userNo+"/updateStatus", {} , JSON.stringify(chatRoomJoin));
        });
        setWebSocket(stompClient);
        

        return () => {
            // 컴포넌트 소멸시 스톰프 클라이언트 연결해제
            let chatRoomJoin = {
                chatRoomNo,
                userNo : user.userNo ,
                userStatus : "2"
            }
            stompClient.send("/chat/chatRoomJoin/"+chatRoomNo+"/member/"+user.userNo+"/updateStatus", {} , JSON.stringify(chatRoomJoin));
            stompClient.disconnect(() => {
                //alert("로그인 다시해주십셔.");
                //navi("/chat/list");
            });
        }
    },[])

    const sendMessage = () => {
        const chatMessage = {
            message ,
            chatRoomNo, 
            userNo : user.userNo
        }

        if(!message){
            alert("뭐든 입력하세여");
            return;
        }
        if(!user){
            alert("로그인 좀 하세요");
            return;
        }
        if(!webSocket){
            alert("웹소켓 연결중입니다.")
            return;
        }
        //웹소켓 서버로 데이터 전송
        webSocket.send("/chat/sendMessage/chatRoomNo/"+chatRoomNo, {}, JSON.stringify(chatMessage));
        setMessage("");
    }

    const scrollToBottom = () => {
        if(bottomRef.current){
            bottomRef.current.scrollIntoView({behavior: 'smooth'});
        }
    }

    const submitMessage = (e) =>{
        if(e.key === 'Enter' && !e.shiftKey){
            e.preventDefault();//
            sendMessage();
            //비동기함수와 동일하게 작동시키기위해 지연시간을 추가함.
            // setTimeout(()=>{
            //     textareaRef.current.value ="";                
            // },100);
            setTimeout(() => {
                scrollToBottom();
            }, 100);
        }
    }
    
    function exitChatRoom(){
        webSocket.send(`/chat/chatRoomJoin/${chatRoomNo}/${user.userNo}/delete` , {} , JSON.stringify(user));
        setTimeout(() => {
            navi("/chat/list");
        }, 100);
        //1) 채팅방나가기 -> db에서 chat_room_join테이블에서 채팅방번호와 userno에 해당하는 데이터를 삭제.
        //2) 웹소켓의 다른 사용자에게 사용자가 퇴장하였음을 알리고
        //3) 채팅목록페이지로 이동
    }

    return (
        <>
            {/* 채팅방 참여자 목록을 보여주는 부분 */}
            <ChatRoomMembers chatRoomMembers={chatRoomMembers} />
            <div className="chatting-area">
                <div className="chat-header">
                    <button className="btn btn-outline-danger" onClick={exitChatRoom}>나가기</button>
                </div>

                <ul className="display-chatting">
                    <Messages chatMessages={chatMessages}/>
                    <li ref={bottomRef}></li>
                </ul>

                <div className="input-area">
                    <textarea rows="3" name='message' ref={textareaRef}
                        onKeyDown={submitMessage}
                        onChange={(e) => {
                            setMessage(e.target.value)
                        }}
                        value={message}
                    ></textarea>
                    <button onClick={sendMessage}>전송</button>
                </div>
            </div>
        </>
    )
}