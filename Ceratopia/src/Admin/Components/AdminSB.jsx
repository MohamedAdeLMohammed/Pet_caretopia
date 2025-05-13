import { Link } from "react-router-dom";
import petlogo from '/Ceratopia/Ceratopia/src/assets/pawprint.png'
import logout from '/Ceratopia/Ceratopia/src/assets/logout.png'
import Adoptrequestlogo from '/Ceratopia/Ceratopia/src/assets/question.png'
import community from '/Ceratopia/Ceratopia/src/assets/community.png'
import { useNavigate } from 'react-router-dom';
function AdminSB(){
        const navigate = useNavigate();
        const handleLogout = ()=>{
        sessionStorage.removeItem('token');
        navigate('/login');
    }
    return(
        <>
        <div className="pto-sb">
            <Link to={'/dashboard/storeMangement'} className="d-flex jusitify-content-center db-btn">
            <img src={petlogo} alt="" />
            <p>Mange Store</p>
            </Link>
            {/* <Link to={'/dashboard/adoptationRequests'} className="d-flex jusitify-content-center db-btn">
            <img src={Adoptrequestlogo} alt="" />
            <p>Adopt Requests</p>
            </Link>
            <Link to={'/dashboard/community'} className="d-flex jusitify-content-center db-btn">
            <img src={community} alt="" />
            <p>Community</p>
            </Link>
            <Link to={'/dashboard/adoptationOffers'} className="d-flex jusitify-content-center db-btn">
            <img src={petlogo} alt="" />
            <p>Adopt Offers</p>
            </Link>
            <Link to={'/dashboard/petrecoginition'} className="d-flex jusitify-content-center db-btn">
            <img src={petlogo} alt="" />
            <p>Pet Recognition</p>
            </Link>
            <Link to={'/dashboard/store'} className="d-flex jusitify-content-center db-btn">
            <img src={petlogo} alt="" />
            <p>Store</p>
            </Link> */}
            <button to={''} className="d-flex jusitify-content-center db-btn sign-out-btn"
            onClick={handleLogout}>
            <img src={logout} alt="" />
            <p>Sign Out</p>
            </button>
        </div>
        </>
    );
}
export default AdminSB;