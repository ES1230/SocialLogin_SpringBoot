import { useState , useEffect, useContext} from "react";
import { Link, useParams } from "react-router-dom";

import {Context} from '../App';

export default function BoardDetail({모든데이터}){    
    //url창에 글번호 뽑아오는법
    const {bno} = useParams(); // /:파라미터명 

    let 테스트 = useContext(Context);
    console.log(테스트);

    // 상세보기 불러오는코드변경 
    // bno를 활용해서 게시글배열에서 내가 bno와 일치하는 게시글을 찾아서 넣어주기
    let 상세보기 = 모든데이터.게시글배열.find( function(게시글) {
      return 게시글.글번호 == bno;
    })

    //useEffect 테스트
    let [count, setCount] = useState(0);
    useEffect(function (){
      console.log("첫 로딩 업데이트 이후 실행", count);
      let time = setTimeout(function (){
        console.log(count);
        setCount(count+1);
      } , 2000);
      return function() {
        clearTimeout(time);
      }         
    })


    return (
      <>
        <h2>게시판상세보기</h2>
        <table className='detail-table'>
          <tr onClick={()=> setCount(count+1)}>
            <th colSpan={4}>{상세보기.글제목}</th>
          </tr>
          <tr>
            <th>작성자</th>
            <td>{상세보기.작성자}</td>
            <th>작성일</th>
            <td>{상세보기.작성일}</td>
          </tr>
          <tr>
            <th>글내용</th>
            <td colSpan={3} style={{ height: "200px" }}>{상세보기.글내용}</td>
          </tr>
        </table>
        <div className='btn-area'>
          <Link to={'/board/update/'+상세보기.글번호}>수정</Link>
        </div>
    </>
    // react-strap
    )
}