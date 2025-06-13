import { Link, useNavigate, useLocation } from "react-router-dom";
import { useState } from 'react';
import Swal from 'sweetalert2';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // Check user status first
            const statusRes = await axios.get(`https://localhost:8088/users/email/${email}`, {
                headers: { 'Content-Type': 'application/json' }
            });
            
            const userData = statusRes.data;
            const userStatus = userData.userStatus;

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

            // Proceed with login
            const response = await axios.post('https://localhost:8088/auth/login', {
                email,
                password,
            }, {
                headers: { 'Content-Type': 'application/json' }
            });

            const token = response.data.token;
            sessionStorage.setItem("token", token);

            // Decode token to get user role
            const decodedToken = jwtDecode(token);
            const userRole = decodedToken.role;

            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Login successful",
                showConfirmButton: false,
                timer: 1500
            });

            // Handle redirect based on user role
            if (userRole === 'ADMIN') {
                navigate("/dashboard", { replace: true });
            } else {
                const redirectPath = getRedirectPath();
                navigate(redirectPath, { replace: true });
            }

        } catch (error) {
            console.error("Login error:", error);
            
            let errorMessage = "Incorrect Password or Email";
            if (error.response?.status === 404) {
                errorMessage = "Email not found";
            } else if (error.response?.status === 401) {
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

    const getRedirectPath = () => {
        // 1. Check for state from navigation
        if (location.state?.from) {
            return location.state.from;
        }
        
        // 2. Check session storage
        const storedRedirect = sessionStorage.getItem('redirectAfterLogin');
        if (storedRedirect) {
            sessionStorage.removeItem('redirectAfterLogin');
            return storedRedirect;
        }
        
        // 3. Default to home page for regular users
        return "/";
    };

    return (
        <div className="login-container">
            <div className="login-form-section">
                <h2>Welcome back!</h2>

                <form className="login-form" onSubmit={handleSubmit}>
                    <label>Email address</label>
                    <input 
                        type="email" 
                        placeholder="Enter your email" 
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <label>Password</label>
                    <input 
                        type="password" 
                        placeholder="Enter your password" 
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <div className="forgot">
                        <Link to="/reset-password">Forgot password?</Link>
                    </div>

                    <button className="login-button" type="submit">Login</button>

                    <p className="signup-text">
                        Don't have an account? <Link to="/signup">Sign Up</Link>
                    </p>
                </form>
            </div>
        </div>
    );
}

export default LoginForm;