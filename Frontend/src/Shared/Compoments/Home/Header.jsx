import { Link, useNavigate } from "react-router-dom";
import { React, useState,useEffect } from 'react';
import axios from 'axios';
import profileimage from "../../../assets/profile image.png";
import { jwtDecode } from "jwt-decode";
import { FaFacebookF, FaInstagram, FaTwitter, FaPhoneAlt, FaMapMarkerAlt } from "react-icons/fa";
function Header() {
  const token = sessionStorage.getItem("token");
  const isLoggedIn = !!token;
  const navigate = useNavigate();
  let decode = {};
const [userInfo,setUserInfo] = useState("");
  useEffect(() => {
    const getUserInfo = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/users/user/${decode.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        const info = response.data;
        setUserInfo(info);
      } catch (error) {
        console.error(error);
      }
    };

    getUserInfo();
    console.log(userInfo.userImageProfile)
  }, [decode.id, token]);
    if (isLoggedIn) {
        try {
            decode = jwtDecode(token);
        } catch (error) {
            console.error("Invalid token", error);
            // Optionally remove invalid token
            sessionStorage.removeItem("token");}}
  if (isLoggedIn) {
    try {
      decode = jwtDecode(token);
    } catch (error) {
      console.error("Invalid token", error);
      sessionStorage.removeItem("token");
    }
  }

  const handleLogout = () => {
    sessionStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div className="header">
      {/* <div className="top-bar">
        <div className="contact-info">
          <div className="info-item"><FaPhoneAlt /><span>+02 249 71511</span></div>
          <div className="info-item"><FaMapMarkerAlt /><span>Nasr City, Cairo</span></div>
        </div>
        <div className="social-icons">
          <a href="https://facebook.com" target="_blank" rel="noopener noreferrer">
            <FaFacebookF />
          </a>
          <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">
            <FaInstagram />
          </a>
          <a href="https://twitter.com" target="_blank" rel="noopener noreferrer">
            <FaTwitter />
          </a>
        </div>
      </div> */}

      <div className="main-header">
        <div className="logo-section">
          <span className="home-title">Pet CareTopia</span>
        </div>

{token?(<div className="nav-links">
        
          {decode.role === "ADMIN"?(<Link to="/dashboard">Dashboard</Link>):(
            <>
            <Link to="/home">Home</Link>
            <Link to="/dashboard">Dashboard</Link>
          <Link to="/home/petServices">Services</Link>
            <Link to="/home/store">Store</Link>
          <Link to="/home/shelters">Shelters</Link>
          <Link to="/home/adoption">Adoption</Link>
          <Link to="/home/petRecogintion">Pet Recogintion</Link>
            </>
)}

        </div>):(<div className="nav-links">
          <Link to="/home">Home</Link>
          <Link to="/home/petServices">Services</Link>
          <Link to="/home/store">Store</Link>
          <Link to="/home/shelters">Shelters</Link>
          <Link to="/home/adoption">Adoption</Link>
          <Link to="/home/petRecogintion">Pet Recogintion</Link>
        </div>)}
{/* <div className="nav-links">
          <Link to="/home">Home</Link>
          <Link to="/home/petServices">Services</Link>
          <Link to="/home/store">Store</Link>
          <Link to="/home/shelters">Shelters</Link>
          <Link to="/home/adoption">Adoption</Link>
          <Link to="/home/petRecogintion">Pet Recogintion</Link>
        </div> */}
        {isLoggedIn && decode.name ? (
          <div className="d-flex align-items-center gap-3">
 <Link to={'/dashboard/userProfile'} className="user-profile-link">

<img
  src={userInfo.userImageProfile || profileimage}
  alt="Profile"
  className="profile-avatar"
  onError={(e) => {
    e.target.onerror = null;
    e.target.src = profileimage;
  }}
/>
                    <span>{decode.name}</span></Link>
            <button 
              className="d-flex align-items-center gap-2 sign-out-btn"
              onClick={handleLogout}
            >
              <span>Log Out</span>
            </button>
          </div>
        ) : (
          <div className="d-flex header-btns align-items-center gap-3">
            <Link to={"/login"} className="button-header sign-up">
              Login
            </Link>
            <Link to={"/signup"} className="button-header sign-up">
              Signup
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}

export default Header;