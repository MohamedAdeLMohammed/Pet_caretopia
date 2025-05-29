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
      const response = await axios.post('https://localhost:8088/auth/login', {
        email:email, 
        password:password,
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      console.log('Login successful:', response.data);
      console.log(response.data.token);
      const token = response.data.token;
      sessionStorage.setItem("token",token);
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "You logined Successfully",
        showConfirmButton: false,
        timer: 1500
      });
      navigate("/dashboard")
      // if(isAdmin){
      //   console.log("sddshgfsdhfh")
      //   navigate("/admin-dashboard");
      // }else if (isVaxCenter){
      //   navigate("/center-dashboard");
      // }else{
      //   navigate("/");
      // }

    } catch (error) {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Incorrect Password or Incorrect Email",
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
             
          <div className="remember-row">
            <input type="checkbox" />
            <span>Remember for 30 days</span>
          </div>

          <button className="login-button">Login</button>

          <div className="divider">or</div>

          <div className="social-buttons">
            <button className="google">Sign in with Google</button>
            <button className="apple">Sign in with Apple</button>
          </div>

          <p className="signup-text"> Do not have an account?<Link to={'/Signup'}>
                      <p>Sign Up</p>
                      </Link>
          </p>
          
        </form>
      </div>

      <div className="image-side">
         <img src="/src/assets/login.png" alt="Happy pet" />
        <div className="image-text">Because every pet deserves a happy home.</div>
        </div>

    </div>
  );
}

export default LoginForm;