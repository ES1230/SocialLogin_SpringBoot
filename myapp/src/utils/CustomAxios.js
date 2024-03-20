import axios from 'axios';
import { getCookie } from './Cookie';

//axios모듈 재정의. axios전송시 항상 header에 jwt토큰정보를 추가시킴

const CustomAxios = axios.create();

CustomAxios.interceptors.request.use(function (request) {
    request.headers.Authorization = getCookie('accessToken');
    return request;
});

// 유효한 토큰이 아닐경우, 재로그인 하도록 페이지 리다이렉트
CustomAxios.interceptors.response.use(
    function (response) {
        return response; // 문제없는경우 정상실행
    } ,
    async function (error) { // 에러발생시 실행할 함수
        //응답상태 : 403(권한없음)
        const { response: { status } } = error;

        // 403에러가 뜨는경우?
        // 1) URL에대한 사용권한이 없는경우
        // 2) 로그인을 하지 않은경우
        // 3) JWT가 만료된경우
        if (status === 403) {
            window.location.href = "/auth/login";
        }
    }
)

export default CustomAxios;