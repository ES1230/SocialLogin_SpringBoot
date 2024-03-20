import { Cookies } from 'react-cookie';

const cookies = new Cookies();

export const setCookie = (name, value) => {
    return cookies.set(name , value , { maxAge : 60 * 60 * 3  , path : '/'})
}

export const getCookie = (name) => {
    return cookies.get(name);
}

export const removeCookie = (name) => {
    cookies.set(name, "" , {maxAge : 0 , path :'/'})
}