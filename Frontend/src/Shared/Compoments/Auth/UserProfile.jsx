import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';
import "../../CSS/UserProfile.css";

function UserProfile() {
  const { id: productId } = useParams();
  const [name, setName] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [userPhoneNumber, setUserPhoneNumber] = useState("");
  const [userAddress, setUserAddress] = useState("");
  const [serviceProviderType, setServiceProviderType] = useState("");
  const [serviceProviderExperience, setServiceProviderExperience] = useState("");
  const [serviceProviderSalary, setServiceProviderSalary] = useState(0);
  const [age, setAge] = useState("");
  const [birthDate, setBirthDate] = useState("");
  const [userGender, setUserGender] = useState("");
  const [imageFiles, setImageFiles] = useState([]);

  const token = sessionStorage.getItem("token");
  const decode = jwtDecode(token);
  const isServiceProvider = decode.role === "SERVICE_PROVIDER";

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const url = isServiceProvider
          ? `https://localhost:8088/serviceProviders/user/${decode.id}`
          : `https://localhost:8088/users/user/${decode.id}`;

        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });

        const user = response.data;
        console.log(user);
        setName(user.name);
        setUserEmail(user.userEmail);
        setAge(user.userAge);
        setUserGender(user.userGender);
        setBirthDate(user.birthDate);
        setUserPhoneNumber(user.userPhoneNumber);
        setUserAddress(user.userAddress);

        if (isServiceProvider) {
          setServiceProviderType(user.serviceProviderType);
          setServiceProviderSalary(user.serviceProviderSalary);
          setServiceProviderExperience(user.serviceProviderExperience);
        }
      } catch (error) {
        console.error("Error fetching user:", error);
        Swal.fire("Error", "Could not load user data", "error");
      }
    };

    fetchUser();
  }, [productId, isServiceProvider, decode.id, token]);

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const updateServiceProvider = async () => {
    if (!token) {
      Swal.fire("Unauthorized", "You must be logged in", "error");
      return;
    }

    try {
      const user = {
        name,
        userAddress,
      };

      if (isServiceProvider) {
        user.serviceProviderType = serviceProviderType;
        user.serviceProviderExperience = serviceProviderExperience;
        user.serviceProviderSalary = serviceProviderSalary;
      }

      const formData = new FormData();
      formData.append("user", new Blob([JSON.stringify(user)], { type: "application/json" }));
      imageFiles.forEach((file) => {
        formData.append("image", file);
      });

      const url = isServiceProvider
        ? `https://localhost:8088/serviceProviders/user/${decode.id}`
        : `https://localhost:8088/users/user/${decode.id}`;

      const response = await axios.put(url, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        icon: "success",
        title: "User updated successfully",
        text: `User ID: ${response.data.id}`,
      });
    } catch (error) {
      console.error("Error updating user:", error);
      Swal.fire({
        icon: "error",
        title: "Failed to update user",
        text: error.response?.data?.message || "An error occurred",
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    updateServiceProvider();
  };

  return (
    <div className="profile-container">
      <h2 className="profile-title">Profile Information</h2>
      <form onSubmit={handleFormSubmit} className="profile-form">
        <div className="profile-grid">
          <div className="form-group full-width">
            <label className="form-label">Name</label>
            <input
              type="text"
              className="form-control"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>

          <div className="form-group full-width">
            <label className="form-label">Email</label>
            <input className="form-control" value={userEmail} disabled />
          </div>

          <div className="form-group">
            <label className="form-label">Age</label>
            <input className="form-control" value={age} disabled />
          </div>

          <div className="form-group">
            <label className="form-label">Birth Date</label>
            <input className="form-control" value={birthDate} disabled />
          </div>

          <div className="form-group">
            <label className="form-label">Gender</label>
            <input className="form-control" value={userGender} disabled />
          </div>

          <div className="form-group">
            <label className="form-label">Phone Number</label>
            <input className="form-control" value={userPhoneNumber} disabled />
          </div>

          <div className="form-group full-width">
            <label className="form-label">Address</label>
            <input
              type="text"
              className="form-control"
              value={userAddress}
              onChange={(e) => setUserAddress(e.target.value)}
            />
          </div>

          {isServiceProvider && (
            <div className="service-provider-section">
              <div className="form-group">
                <label className="form-label">Salary</label>
                <input
                  type="number"
                  step="0.01"
                  className="form-control"
                  value={serviceProviderSalary}
                  onChange={(e) => setServiceProviderSalary(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Experience (years)</label>
                <input
                  type="number"
                  step="0.01"
                  className="form-control"
                  value={serviceProviderExperience}
                  onChange={(e) => setServiceProviderExperience(e.target.value)}
                  required
                />
              </div>

              <div className="form-group full-width">
                <label className="form-label">Service Provider Type</label>
                <select
                  className="form-select"
                  value={serviceProviderType}
                  onChange={(e) => setServiceProviderType(e.target.value)}
                  required
                >
                  <option value="VET">VET</option>
                  <option value="TRAINER">TRAINER</option>
                  <option value="SITTER">SITTER</option>
                </select>
              </div>
            </div>
          )}

          <div className="form-group full-width">
            <label className="form-label">Upload New Images</label>
            <input
              type="file"
              className="form-control"
              multiple
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>

          <div className="form-actions">
            <button
              type="submit"
              className="btn-Update"
            >
              Update Profile
            </button>
            <button
              type="reset"
              className="btn btn-danger"
              onClick={() => {
                setName("");
                setServiceProviderSalary(0);
                setServiceProviderExperience("");
                setServiceProviderType("");
                setImageFiles([]);
              }}
            >
              Reset
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default UserProfile;