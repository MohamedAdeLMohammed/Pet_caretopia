import { Link } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import { React, useState } from 'react';
import Swal from 'sweetalert2';
import { toast, ToastContainer } from 'react-toastify';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    // 1. Get user by email to check status
    const statusRes = await axios.get(`https://localhost:8088/users/email/${email}`, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    // The user data is the response data itself, not nested under a property
    const userData = statusRes.data;
    console.log("Full user data:", userData); // Debugging
    
    const userStatus = userData.userStatus;
    console.log("User status:", userStatus); // Debugging

    if (userStatus === 'BANNED') {
      Swal.fire({
        icon: 'error',
        title: 'Access Denied',
        text: 'Your account has been banned.',
      });
      return;
    }

    if (userStatus === 'INACTIVE') {
      Swal.fire({
        icon: 'warning',
        title: 'Inactive Account',
        text: 'Your account is inactive. Please contact support.',
      });
      return;
    }

    // Rest of your login logic...
    const response = await axios.post('https://localhost:8088/auth/login', {
      email,
      password,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const token = response.data.token;
    sessionStorage.setItem("token", token);

    Swal.fire({
      position: "top-end",
      icon: "success",
      title: "You logged in successfully",
      showConfirmButton: false,
      timer: 1500
    });

    navigate("/dashboard");

  } catch (error) {
    console.error("Login error:", error); // Debugging
    
    let errorMessage = "Incorrect Password or Email";
    if (error.response && error.response.status === 404) {
      errorMessage = "Email not found";
    } else if (error.response && error.response.status === 401) {
      errorMessage = "Incorrect password";
    }

    Swal.fire({
      position: "top-end",
      icon: "error",
      title: errorMessage,
      showConfirmButton: false,
      timer: 1500
    });
  }
};
  return (
    <div className="login-container">
      <div className="login-form-section">
        <h2>Welcome back!</h2>

        <form className="login-form" onSubmit={handleSubmit}>
          <label>Email address</label>
          <input type="email" placeholder="Enter your email" value={email}
          onChange={(e) => setEmail(e.target.value)}/>

          <label>Password</label>
          
            <input type="password" placeholder="Enter your password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            />
           

            <div className="forgot" >
            <Link to={'/reset-password'}>forgot password</Link>
            </div>
             

          <button className="login-button">Login</button>


          <p className="signup-text"> Do not have an account?<Link to={'/Signup'}>
                      <p>Sign Up</p>
                      </Link>
          </p>
          
        </form>
      </div>


    </div>
  );
}

export default LoginForm;