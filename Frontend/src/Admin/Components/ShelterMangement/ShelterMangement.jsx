import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import { jwtDecode } from 'jwt-decode'; // Add this import
import "../../CSS/AdminDashboard.css";
import shelterImage from "../../../assets/shelter.jpg";

function ShelterManagement() { // Fixed typo in component name
  const [shelters, setShelters] = useState([]);
  const [isAdmin, setIsAdmin] = useState(false); // Added admin state
  const token = sessionStorage.getItem("token");
  
  useEffect(() => {
    if (token) {
      try {
        const decode = jwtDecode(token);
        // Check if user is admin (adjust this based on your token structure)
        setIsAdmin(decode.role === 'ADMIN'); // Or whatever your admin check is
      } catch (error) {
        console.error("Invalid token", error);
        sessionStorage.removeItem('token');
      }
    }
  }, [token]);

  useEffect(() => {
    const getShelters = async () => {
      try {
        const config = token ? {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        } : {};
        
        const response = await axios.get(`https://localhost:8088/shelters`, config);
        setShelters(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load shelters", "error");
      }
    };

    getShelters();
  }, [token]);

  const addShelter = () => {
    Swal.fire({
      title: "Add Shelter",
      html: `
        <input type="text" id="swal-name" class="swal2-input" placeholder="Shelter Name" required />
        <input type="text" id="swal-location" class="swal2-input" placeholder="Location" required />
        <input type="text" id="swal-contact" class="swal2-input" placeholder="Contact Number" required />
        <input type="email" id="swal-email" class="swal2-input" placeholder="Email" required />
        <input type="text" id="swal-website" class="swal2-input" placeholder="Website URL" />
        <textarea id="swal-description" class="swal2-textarea" placeholder="Description"></textarea>
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: "Add",
      confirmButtonColor: "#023C5A", 
      cancelButtonColor: "#6c757d",
      preConfirm: async () => {
        const name = document.getElementById("swal-name").value.trim();
        const location = document.getElementById("swal-location").value.trim();
        const contactNumber = document.getElementById("swal-contact").value.trim();
        const email = document.getElementById("swal-email").value.trim();
        const websiteUrl = document.getElementById("swal-website").value.trim();
        const description = document.getElementById("swal-description").value.trim();

        if (!name || !location || !contactNumber || !email) {
          Swal.showValidationMessage("Please fill in all required fields.");
          return false;
        }

        try {
          await axios.post("https://localhost:8088/shelters", {
            name,
            location,
            contactNumber,
            email,
            websiteUrl,
            description
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          });
          return true;
        } catch (error) {
          console.error("Error adding shelter:", error);
          Swal.showValidationMessage(error.response?.data?.message || "Failed to add shelter.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Shelter added successfully!", "success")
          .then(() => window.location.reload());
      }
    });
  };

  return (
    <div className="store-management-container">
                    {token?( <h2 className="management-dashboard-title">Shelter Management</h2>):(<h2 className="management-dashboard-title">Shelters</h2>)}
      
      {/* Only show Add Shelter button for admin users */}
      {isAdmin && (
        <button onClick={addShelter} className="Add-btn">
          Add Shelter
        </button>
      )}

      {shelters.length === 0 ? (
        <p style={{ marginTop: "20px" }}>No shelters found.</p>
      ) : (
        <div className="management-grid">
          {shelters.map((shelter) => (
            <div key={shelter.id} className="management-card">
              <img src={shelterImage} alt="Shelter" />
              <h4 className="management-card-title">{shelter.name}</h4>
              <p>{shelter.location}</p>
              {token?(              <Link to={`shelter/${shelter.id}`} className="management-card-button">
                View Shelter
              </Link>):(null)}

            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ShelterManagement;