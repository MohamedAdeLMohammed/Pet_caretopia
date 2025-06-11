import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import "../../CSS/AdminDashboard.css";
import shelterImage from "../../../assets/shelter.jpg";


function ShelterMangement() {
  const [shelters, setShelters] = useState([]);
  const token = sessionStorage.getItem("token"); // Replace with your actual token logic

  useEffect(() => {
    const getShelters = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/shelters`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setShelters(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load shelters", "error");
      }
    };

    getShelters();
  }, []);

  const addShelter = () => {
    Swal.fire({
      title: "Add Shelter",
      html: `
        <input type="text" id="swal-name" class="swal2-input" placeholder="Shelter Name" />
        <input type="text" id="swal-location" class="swal2-input" placeholder="Location" />
        <input type="text" id="swal-contact" class="swal2-input" placeholder="Contact Number" />
        <input type="email" id="swal-email" class="swal2-input" placeholder="Email" />
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
          Swal.showValidationMessage("Failed to add shelter.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Shelter added successfully!", "success").then(() => {
          window.location.reload(); // Optional: reload to refresh list
        });
      }
    });
  };

  return (
    <>
       <div className="store-management-container">
        <h2 className="management-dashboard-title">Shelter Management</h2>
        <button onClick={addShelter} className="Add-btn">Add Shelter</button>

        {shelters.length === 0 ? (
          <p style={{ marginTop: "20px" }}>No shelters found. Please add one.</p>
        ) : (
          <div className="management-grid">
            {shelters.map((shelter) => (
              <div key={shelter.id} className="management-card">
               <img src={shelterImage} alt="Shelter" />
                <h4 className="management-card-title">{shelter.name}</h4>
                <Link to={`shelter/${shelter.id}`} className="management-card-button">
                  Manage Shelter
                </Link>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}

export default ShelterMangement;
