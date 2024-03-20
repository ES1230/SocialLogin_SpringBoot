import { Outlet } from "react-router-dom"
export default function Outer(){
    return (
        <div className="outer">
            <br/>
            <Outlet></Outlet>
        </div>
    )
}