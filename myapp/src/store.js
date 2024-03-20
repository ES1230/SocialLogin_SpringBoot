//state보관소

import { configureStore, createSlice } from "@reduxjs/toolkit";
import { getCookie ,removeCookie} from "./utils/Cookie";

let init = () => {
    let user = getCookie('user');
    return user ? user : null;
}

let user = createSlice({
    name : 'user', // state이름
    initialState : init(), // state초기값
    // let [user , ?] = useState({email : ... , nickname:...})
    reducers : { // reducers : state의 값을 변경해주는 함수"들"을 정의해두는 부분. 모든 reducers함수들의 첫번째 매개변수는
    // 현재 state값이 깊은복사된 형태로 전달된다.
        프로필랜덤변경(state) {
            let random = Math.floor(Math.random() * 5);//0~5랜덤값
            state.profile = `/images/user${random}.jpg`;
            return state;
        }, 
        로그아웃() {
            removeCookie("user");
            removeCookie("accessToken");
            return null;
        } ,
        로그인(state , 데이터){
            return 데이터.payload;
        }
    }
});

export default configureStore({
    reducer : {
        user : user.reducer // 변수명.reducer
    }
})

export let {프로필랜덤변경 , 로그아웃 , 로그인} = user.actions; // reducers내부에 작성한 코드들은 actions라는 객체안에 담겨있음.