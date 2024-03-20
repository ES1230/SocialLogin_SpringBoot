/* eslint-disable */
import './App.css';
import {createContext, useEffect, useState} from 'react';

import 초기게시글 from './data';// default옵션으로 export한경우 변수명을 다르게 가져올수 있음
import BoardInsert from './pages/BoardInsert';
import BoardList from './pages/BoardList';
import BoardDetail from './pages/BoardDetail';
import BoardUpdate from './pages/BoardUpdate';
import { Route, Routes, Link , useNavigate } from 'react-router-dom';
import Outer from './components/Outer';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import {프로필랜덤변경, 로그아웃} from './store';
import LoginModal from './components/LoginModal';
import ChattingRoomList from './pages/ChattingRoomList';
import ChattingRoom from './pages/ChattingRoom';
import KakaoLoginForm from './pages/KakaoLoginForm';
export let Context = createContext(); // state저장소

function App() {
  /* 
    useEffect : 컴포넌트가 렌더링될 때를 감지하여 렌더링된 "이후" 실행할 코드를 기술하는 함수.
    컴포넌트에는 기본적으로 lifeCycle이라는 개념이 있는데 , 
    컴포넌트가 처음 로딩되는 시기를 mount
    state변경에 의해 컴포넌트가 재 랜더링 될대는 update
    컴포넌트가 교체/소멸 될때는 unmount라고 부름
    useEffect는 mount,update,unmount되는 시점에 각각 끼어들어서 내가 실행하고자 하는 코드를 추가할 수 있다.

    사용방법
    useEffect( function() => {
      랜더링이 완료된 "후!!!!!!!!!!!!!!!!" 실행할 코드

      return 컴포넌트가 재랜더링되거나, 소멸할때 실행할 "함수"
    } , [의존성 목록]) // state값들을 배열형태로 넣어줌. 의존성목록에 들어간 state변수의 값이 바뀌면(update)
    useEffect내부의 함수가 다시 호출된다.
  */
  /* 
    ContextApi : 복잡한 컴포넌트구조에서 state상태를 손쉽게 전달하도록 도와주는 문법.
    ex) App의 자식 BoardDetail의 자식 BoardDetailDetail에 데이터를 전달해줘야한다?
    props를 중첩으로 전달, 또 전달, 또전달해주는 코드를 짜야함. 
  */
  /* 
    store에서 데이터 꺼내오기
    useSelector : store에 저장되어있는 state를 꺼내오는 함수
    let user = useSelector( (state) => {return state} );
  */
  let user = useSelector( ({user}) => {return user} );
  let 전송 = useDispatch();

  let [loginModal , setLoginModal] = useState(false);

  let [게시글배열, 게시글배열변경함수] = useState(null);
  let navi = useNavigate();

  useEffect(function(){
    /*
      axios ? react에서 가장많이 사용되는 비동기함수를 지원하는 라이브러리      

      axios
      .get/post('url경로' , {전달한데이터})
      .then( function (result) {
        //요청 성공시 실행할 코드
      })
      .then
      .then..
      .catch(function(error){
        //요청 실패시 실행할 코드
      })
    */
    //axios.get('https://my-json-server.typicode.com/alsrudals2013/react/board')
    axios.get("/data/data.json",{data: "필요한데이터 아무거나 key:value형태로 넣어주기"})
    .then(
      function(result){
        게시글배열변경함수(result.data);
        //console.log(result.data);
      }
    ).catch((error) => console.log(error.response))
  },[]);
  /*
    의존성 목록을 빈배열로 두는경우 첫 로딩시에만 useEffect함수 내부의 내용이 실행됨
    의존성 목록에 내가 원하는 데이터만 지정하는경우 해당 데이터가 바뀔때만 useEffect함수 내부의 내용이 실행됨.
  */
  
  //let [상세보기 ,상세보기변경] = useState(null);
  let 등록페이지url = "/insert";

  let 모든데이터 = {
    게시글배열 ,
    게시글배열변경함수 ,  
  }

  function 모달창열기(){
    setLoginModal(true);
  }

  function 모달창닫기(){
    setLoginModal(false);
  }
  console.log(user)
  return (
    <div className="App">
      {/*
         jsx문법 내부에서의 주석
          JSX문법)
          js문법 내부에 html코드를 작성할 수 있는 문법을 jsx문법이라고함.
          리액트에서 ui를 구성할 때 보편적으로 사용되는 방법으로 복잡한 코드를 짤 필요 없이,
          동적으로 추가되는 dom요소를 단순 코드선언으로 생성할 수 있게 도와준다.

          로그아웃기능 만들기 -> user state를 없는것으로 만들기
          로그아웃시 아래 유저정보대신 로그인버튼이 보이게 작업
      */}
      <div className="header">
        <div className="header-1">
          <h3 style={ { fontWeight : "bolder" } }>{'KH E CLASS'}</h3>
        </div>
        <div className='header-2'>
          {
          user ? (
                  <>
                    <img src={user.profile} onClick={ () => 전송(프로필랜덤변경())  }/>
                    <div className='user-info'>
                      <span className='user-nickname'>{user.nickname}</span>
                      <span className='user-email'>{user.email}</span>
                    </div>
                    <button onClick={() => 전송(로그아웃())}>로그아웃</button>
                  </>
                  ) :
                  <button onClick={() => navi("/auth/login")}>로그인</button>
          }
        </div>
      </div>
      <div className='nav'>
        {/* 
          react방식 event부여
          onClick={함수}
          * 주의점 : 무조건 함수자료형값만 넣어줘야함. 함수호출한 결과값 넣어주면 의미가 없다.          
        */}        
        <Link to="/board/list">게시판</Link>
        <Link to={"/board"+등록페이지url}>등록</Link>     
        <Link to={"/chat/list"}>채팅방</Link>   
      </div>
      <main>
      <Context.Provider value={ {모든데이터}  }>
        <Routes>
          <Route path='/' element={<BoardList 모든데이터={모든데이터} />} />
          <Route path="/board" element={<Outer/>} >
            {/* /board/list */}
            <Route path='list' element={<BoardList 모든데이터={모든데이터} />}/> 
            <Route path="insert" element={<BoardInsert 모든데이터={모든데이터} />}/>
            {/* 
              라우트 파라미터 문법
              /:매개변수명
              중첩으로 여러개 작성도 가능
            */}
            <Route path="detail/:bno" element={<BoardDetail 모든데이터={모든데이터}/>} />
            <Route path="update/:bno" element={<BoardUpdate 모든데이터={모든데이터} />}/>
          </Route>
          <Route path='/chat'>            
            <Route path='list' element={<ChattingRoomList/>}/>
              <Route path="detail/:chatRoomNo" element={<ChattingRoom />} />             
          </Route>
          <Route path='/auth'>
              <Route path="login" element={ <KakaoLoginForm/>  } />
          </Route>  
          <Route path='*' element={
            <div>
              <h1 style={{color: "red"}}>존재하지 않는 페이지입니다.</h1>
              <Link to="/">사이트로돌아가기</Link>
            </div>
          }/>
        </Routes>
        </Context.Provider>
      </main>
    </div>
  );
}



export default App;
