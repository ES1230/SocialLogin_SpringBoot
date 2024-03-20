import KakaoLogin from 'react-kakao-login';
import axios from 'axios';
import { setCookie } from '../utils/Cookie';

import { useDispatch } from 'react-redux';
import { 로그인, 로그아웃 } from '../store';
import { useNavigate } from 'react-router-dom';

const KakaoLoginForm = () => {

    // 여러분 본인 api key
    const kakaoClientId = '9cb146d3569943651c1a6c8f8d2e328c';
    const dispatch = useDispatch();
    const navi = useNavigate();
    
    const kakaoOnSuccess = async(data) => {
        console.log('카카오 서버에서 전달받은 토큰(인가코드) ', data);
        //1. 소셜로그인시 최초 로그인이라면 회원정보를 db에 저장.
        //2. 토큰의 인가코드(access_token)를 백엔드 서버로 전달
        await axios
            .post("http://localhost:8084/api/auth/login/kakao", { idToken: data.response.access_token })
            .then((res) => {
                // res -> 반환받은 jwt토큰이 보관되어있을예정
                console.log('백엔드 서버에서 내려준 토큰 ', res);
                // 전달받은 토큰을 쿠키에 저장.
                setCookie("accessToken", res.data.jwtToken);
                setCookie("user" , JSON.stringify(res.data.user))
                dispatch(로그인(res.data.user))
                navi("/")
            })
            .catch(console.log)
    }

    const kakaoOnFail = (error) => {
        console.log(error);
    }

    return (
        <KakaoLogin
            token={kakaoClientId}
            onSuccess={kakaoOnSuccess}
            onFail={kakaoOnFail}
        />
    )
}

export default KakaoLoginForm;