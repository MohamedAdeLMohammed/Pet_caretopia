import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';
function UpdateProviderRole() {
  const { id: productId } = useParams();
  console.log(productId);
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
        console.log(decode);
        console.log(decode.role)
        console.log(decode.id)
  // Fetch existing product data on mount
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/serviceProviders/user/${decode.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        const serviceProvider = response.data;
        console.log(serviceProvider)
        setName(serviceProvider.name);
        setUserEmail(serviceProvider.userEmail);
        setAge(serviceProvider.age);
        setUserGender(serviceProvider.userGender);
        setBirthDate(serviceProvider.birthDate);
        setUserPhoneNumber(serviceProvider.userPhoneNumber);
        setUserAddress(serviceProvider.userAddress);
        setServiceProviderType(serviceProvider.serviceProviderType);
        setServiceProviderSalary(serviceProvider.serviceProviderSalary);
        setServiceProviderExperience(serviceProvider.serviceProviderExperience);
      } catch (error) {
        console.error("Error fetching product:", error);
        Swal.fire("Error", "Could not load product data", "error");
      }
    };

    fetchProduct();
  }, [productId]);

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const updateServiceProvider = async () => {
    if (!token) {
      Swal.fire("Unauthorized", "You must be logged in as admin", "error");
      return;
    }

    try {
      const user = {
        name:name,
        userAddress,
        serviceProviderType:serviceProviderType,
        serviceProviderExperience:serviceProviderExperience,
        serviceProviderSalary:serviceProviderSalary,
      };
      const formData = new FormData();
      formData.append("user", new Blob([JSON.stringify(user)], { type: "application/json" }));
      imageFiles.forEach((file) => {
        formData.append("image", file);
      });
      const response = await axios.put(`https://localhost:8088/serviceProviders/user/${decode.id}`,formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        icon: "success",
        title: "Service Provider updated successfully",
        text: `Product ID: ${response.data.id}`,
      });
    } catch (error) {
      console.error("Error updating product:", error);
      Swal.fire({
        icon: "error",
        title: "Failed to update product",
        text: error.response?.data?.message || "An error occurred",
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    updateServiceProvider();
  };

  return (
    <div className="p-3 container mt-5 mb-5">
      <form onSubmit={handleFormSubmit} className="post-form">
        <div className="container row g-3">
          <div className="col-md-12">
            <label className="form-label">Name</label>
            <input
              type="text"
              className="form-control"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="col-md-12">
            <label className="form-label">Email</label>
            <input
              className="form-control"
              rows="3"
              value={userEmail} disabled
            />
          </div>
            <div className="col-md-6">
            <label className="form-label">Age</label>
            <input
              className="form-control"
              rows="3"
              value={age} disabled
            />
          </div>
            <div className="col-md-6">
            <label className="form-label">BirthDate</label>
            <input
              className="form-control"
              rows="3"
              value={birthDate} disabled
            />
          </div>
            <div className="col-md-6">
            <label className="form-label">Gender</label>
            <input
              className="form-control"
              rows="3"
              value={userGender} disabled
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Salery</label>
            <input
              type="number"
              step="0.01"
              className="form-control"
              value={serviceProviderSalary}
              onChange={(e) => setServiceProviderSalary(e.target.value)}
              required
            />
          </div>
            <div className="col-md-6">
            <label className="form-label">Experience</label>
            <input
              type="number"
              step="0.01"
              className="form-control"
              value={serviceProviderExperience}
              onChange={(e) => setServiceProviderExperience(e.target.value)}
              required
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Phone Number</label>
            <input
              type="text"
              className="form-control"
              value={userPhoneNumber}
              disabled
            />
          </div>
          <div className="col-md-12">
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
          <div className="col-md-12">
            <label className="form-label">Upload New Images</label>
            <input
              type="file"
              className="form-control"
              multiple
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
          <div className="d-flex gap-2 mt-3">
            <button
              type="submit"
              className="btn btn-success"
              style={{ background: "#21293a", border: "#21293a" }}
            >
              Update Service Provider
            </button>
            <button
              type="reset"
              className="btn btn-danger"
              onClick={() => {
                setName("");
                setDescription("");
                setPrice(0);
                setStockQuantity(0);
                setCategory("FOOD");
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

export default UpdateProviderRole;
