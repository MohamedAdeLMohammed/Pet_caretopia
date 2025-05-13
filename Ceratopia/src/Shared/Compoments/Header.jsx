import ProjectLogo from '/Ceratopia/Ceratopia/src/assets/Blue Retro Animals & Pets Logo.png'
import { Link, useNavigate } from 'react-router-dom';
import profileimage from '/Ceratopia/Ceratopia/src/assets/profile image.png'
import { jwtDecode } from 'jwt-decode';
function Header(){
    const token = sessionStorage.getItem("token");
    const isLoggedIn = !!token;
    let decode = {};

    if (isLoggedIn) {
        try {
            decode = jwtDecode(token);
        } catch (error) {
            console.error("Invalid token", error);
            // Optionally remove invalid token
            sessionStorage.removeItem("token");
        }
    }

    return (
        <div className='main-header d-flex justify-content-around w-100'>
            <div className='d-flex align-items-center'>
                <img src={ProjectLogo} className='admindash-header-logo rounded-circle'/>
                <h3 className='p-2'>Caretopia</h3>
            </div>
            {isLoggedIn && decode.name ? (
                <div className='d-flex align-items-center gap-3'>
                    <img src={profileimage} className='admindash-header-logo rounded-circle'/>
                    <span>{decode.name}</span>
                </div>
            ) : (
                <div className='d-flex header-btns align-items-center gap-3'>
                    <Link to={'/login'} className='button-header sign-up'>Login</Link>
                    <Link to={'/signup'} className='button-header sign-up'>Signup</Link>
                </div>
            )}
        </div>
    );
}

export default Header;