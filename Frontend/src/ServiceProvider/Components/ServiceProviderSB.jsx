import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import "../CSS/SPDashboard.css";
import {FaHotel ,FaRegCommentDots} from "react-icons/fa";
function VetSB(){
            const navigate = useNavigate();
        const handleLogout = ()=>{
        sessionStorage.removeItem('token');
        navigate('/login');
    }
    return(
    <>
        
        <div className="pto-sb">
            <Link to={"/dashboard/facilities"} className="db-btn">
            <FaHotel/>
            <p>View Facilities</p>
            </Link>
         <Link to={'/dashboard/chat'} className="db-btn">
              <FaRegCommentDots/>
              <p>Chat</p>
            </Link>
        </div>


    </>
    );
}
export default VetSB;