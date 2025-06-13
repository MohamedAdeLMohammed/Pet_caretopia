import { Link } from "react-router-dom";
import { useState } from 'react'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';

function SignupForm() {
  const [namee, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [selectedRole, setSelectedRole] = useState('');
  const [gender, setGender] = useState('');
  const [address, setAddress] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await axios.post('https://localhost:8088/auth/register', {
        name:namee,
        email: email,
        password: password,
        gender:gender,
        phoneNumber:phone,
        birthDate:birthDate,
        role: selectedRole,
        address:address
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      console.log('Login successful:', response.data);
      toast.success("Registered Successfully✅", {
        position: "top-center"
      });
      navigate("/login");
      // put your link here to navigate to your dashboard

    } catch (error) {
      toast.error(error, {
        position: "top-center"
      });
    }
  };
    return (
      <div className="login-container">
        <div className="login-form-section">
          <h2>Get Started Now!</h2>
  
          <form className="login-form" onSubmit={handleSubmit}>
  
            <label>Name</label>
              <input type="text" placeholder="Enter your Name" required value={namee}
              onChange={(e) => setName(e.target.value)}/>
             
            <label>Phone Number</label>
              <input type="number" placeholder="Enter your phone number" value={phone}
              onChange={(e) => setPhone(e.target.value)}   pattern="^[0-9]{10}$" required/>

            <label>Email address</label>
              <input type="email" placeholder="Enter your email"required value={email}
              onChange={(e) => setEmail(e.target.value)} />
                   <label>Email address</label>
              <input type="text" placeholder="Enter your Address"required value={address}
              onChange={(e) => setAddress(e.target.value)} />
            <label>Password</label>
              <input type="password" placeholder="Enter your password" value={password}
              onChange={(e) => setPassword(e.target.value)} required/>
              <label htmlFor="selectedRole" className="form-label">Select Role</label>
              <label htmlFor="birthDate">Birth Date</label>
<input
  type="date"
  id="birthDate"
  name="birthDate"
  value={birthDate}
  onChange={(e) => setBirthDate(e.target.value)}
  required
/>

<select
  id="selectedRole"
  className="form-select"
  value={selectedRole}
  onChange={(e) => setSelectedRole(e.target.value)}
>
  <option value="">Select a role...</option>
  <option value="USER">USER</option>
  <option value="SERVICE_PROVIDER">SERVICE_PROVIDER</option>
</select>
<select
  id="selectedGender"
  className="form-select"
  value={gender}
  onChange={(e) => setGender(e.target.value)}
>
  <option value="">Select Gender...</option>
  <option value="MALE">Male</option>
  <option value="FEMALE">Female</option>
  <option value="OTHER">Other</option>
</select>
  
            <button className="login-button">Sign in </button>
  
  
            <p className="signup-text"> Already have an account? <Link to={'/Login'}>
                      <p>Login</p>
                      </Link></p>
          </form>
        </div>


      </div>
    );
  }
  
  export default SignupForm;
