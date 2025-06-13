import { Link } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import { React, useState } from 'react';
import Swal from 'sweetalert2';
import { toast, ToastContainer } from 'react-toastify';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
function ResetPasswordForm() {
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  const navigate = useNavigate();
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('https://localhost:8088/auth/reset-password', {
        email:email,
        phoneNumber:phoneNumber,
        newPassword:newPassword,
        confirmNewPassword:confirmNewPassword
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      console.log('Login successful:', response.data);
      console.log(response.data.token);
    //   const token = response.data.token;
    //   sessionStorage.setItem("token",token);
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "You logined Successfully",
        showConfirmButton: false,
        timer: 1500
      });
      navigate("/login")
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
                    <label>PhoneNumber</label>
          
            <input type="text" placeholder="Enter your password" 
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            />
          <label>Password</label>
          
            <input type="password" placeholder="Enter your password" 
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            />
            <label>confirmPassword</label>
          
            <input type="password" placeholder="Enter your password" 
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
            />
           

            <div className="forgot" >
            <a href="#">forgot password</a>
            </div>
             

          <button className="login-button">Reset Password</button>

=          
        </form>
      </div>

    </div>
  );
}

export default ResetPasswordForm;