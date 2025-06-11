import { Link } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import {FaRegCommentDots, FaRegCalendarCheck,FaPaw, FaQuestionCircle, FaUsers,FaStore,FaRobot} from "react-icons/fa";
function PTOwnerSB(){
        const navigate = useNavigate();
        const token = sessionStorage.getItem("token");
        const decode = jwtDecode(token);   
        const handleLogout = ()=>{
        sessionStorage.removeItem('token');
        navigate('/login');
    }
    return(
        <>
        <div className="pto-sb">
            <Link to={'/dashboard/pets'} className="db-btn">
             <FaPaw />
            <p>My Pets</p>
            </Link>
            {decode.role === "PET_OWNER"?(<Link to={'/dashboard/adoptationRequests'} className="db-btn">
             <FaQuestionCircle />
            <p>Adoption Requests</p>
            </Link>):null}
            <Link to={'/dashboard/community'} className="db-btn">
            <FaUsers />
            <p>Community</p>
            </Link>
            <Link to={'/dashboard/adoptationOffers'} className="db-btn">
            <FaPaw />
            <p>Adoption Offers</p>
            </Link>
            <Link to={'/dashboard/petrecoginition'} className="db-btn">
            <FaRobot />
            <p>Pet Recognition</p>
            </Link>
            <Link to={'/dashboard/store'} className="db-btn">
            <FaStore/>
            <p>Store</p>
            </Link>
            <Link to={'/dashboard/petServices'} className="db-btn">
             <FaPaw />
            <p>Pet Services</p>
            </Link>
            <Link to={'/dashboard/chat'} className=" db-btn">
            <FaRegCommentDots/>
            <p>Chat</p>
            </Link>


        </div>
        </>
    );
}
export default PTOwnerSB;