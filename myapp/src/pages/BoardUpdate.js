import {useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';

export default function BoardUpdate({모든데이터}){
    // 상세보기 , 배열
    let {게시글배열, 게시글배열변경함수 } = 모든데이터;
    
    let {bno} = useParams(); // {bno : 1}
    let 상세보기 = 게시글배열.find( 게시글 => 게시글.글번호 == bno )

    let [입력값, 입력값변경] =useState(상세보기);
    
    const navigate = useNavigate();

    function 게시글수정(){
        let {글제목, 글내용, 작성자} = 입력값;
        if( !글제목 || !글내용 || !작성자){
            alert("뭐든 입력하세요");
            return;
        }

        // for(let i =0; i<게시글배열.length; i++){
        //     if(게시글배열[i].글번호 == 입력값.글번호){
        //         게시글배열[i] = 입력값;
        //     }
        // }

        const 변경된게시글배열 = 게시글배열.map(function(게시글){
            if(게시글.글번호 == 입력값.글번호) return 입력값;
            return 게시글;
        })
        
        게시글배열변경함수([...변경된게시글배열]);        
        // 레이아웃변경(2); // 상세보기
        //상세보기변경(입력값);
        navigate('/board/detail/'+입력값.글번호);
    }

    function onIputHandler(e){
        let {name , value} = e.target;
        입력값변경({...입력값, [name] : value});
    }   
    return (
        <>
          <h2>수정</h2>
          <table className='enroll-table'>
            <tr>
              <th>제목</th>
              <td colSpan={3}>
                <input type='text' name='글제목'
                onChange={onIputHandler}
                value={입력값.글제목}
                />
              </td>
            </tr>
            <tr>
              <th>작성자</th>
              <td colSpan={3}>
                <input type='text' name='작성자' 
                onChange={onIputHandler}
                value={입력값.작성자}
                />
              </td>
            </tr>
            <tr>
              <th>글내용</th>
              <td colSpan={3} style={ { height: "200px" }  }>
                <textarea name='글내용'
                onChange={onIputHandler}
                value={입력값.글내용}
                ></textarea>
              </td>
            </tr>
            <tr>
              <th colSpan={4}><button onClick={ ()  => 게시글수정()   }>등록</button></th>
            </tr>
          </table>
      </>
    )
}