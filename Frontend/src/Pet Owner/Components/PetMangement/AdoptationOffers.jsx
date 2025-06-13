import { useEffect, useState } from "react";
import axios from 'axios';
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';

function AdoptationOffers() {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  
  const token = sessionStorage.getItem('token');
  let userId = null;
  
  if (token) {
    try {
      const decode = jwtDecode(token);
      userId = decode.id;
    } catch (error) {
      console.error("Invalid token", error);
      sessionStorage.removeItem('token');
    }
  }

  const requestAdoption = (petId) => {
    if (!token) {
      Swal.fire({
        title: "Login Required",
        text: "You must be logged in to request an appointment",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Login",
        cancelButtonText: "Cancel"
      }).then((result) => {
        if (result.isConfirmed) {
          navigate("/login");
        }
      });
      return;
    }

    Swal.fire({
      title: "Send Adoption Request",
      html: `
        <input id="swal-message" class="swal2-input" placeholder="Enter a message" />
      `,
      showCancelButton: true,
      confirmButtonText: "Send Request",
      preConfirm: async () => {
        const message = document.getElementById("swal-message").value.trim();

        try {
          await axios.post("https://localhost:8088/adoptions", {
            message,
            petId
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          return true;
        } catch (error) {
          console.error("Error sending adoption request:", error);
          Swal.showValidationMessage("Failed to send request.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Adoption request sent successfully!", "success");
        // Use navigate instead of reload for better UX
        navigate(0); // This will reload the current page
      }
    });
  };

  useEffect(() => {
    const getPetsOffers = async () => {
      try {
        setLoading(true);
        const response = await axios.get('https://localhost:8088/adoptions/available-for-adoption');
        setPets(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
        Swal.fire("Error", "Failed to load adoption offers", "error");
      } finally {
        setLoading(false);
      }
    };
    getPetsOffers();
  }, []); // Removed token dependency

  // Filter out pets owned by the user (only if logged in)
  const availablePets = token ? pets.filter(pet => pet.ownerId !== userId) : pets;

  if (loading) {
    return <div className="loading-message">Loading adoption offers...</div>;
  }

  return (
    <div className="store-management-container">
      <h2 className="management-dashboard-title">Adoption Offers</h2>
      {availablePets.length === 0 ? (
        <p>No pets available for adoption.</p>
      ) : (
        <div className="management-grid">
          {availablePets.map((pet) => (
            <div className="management-card" key={pet.petID}>
              <img 
                src={pet.imageUrl || "https://via.placeholder.com/150"} 
                alt={pet.petName} 
                className="pet-image"
              />
              <p className="management-card-title">{pet.petName}</p>
              <p>Type: {pet.petTypeName}</p>
              <p>Breed: {pet.petBreedName}</p>
              <button 
                className='management-card-button' 
                onClick={() => requestAdoption(pet.petID)}
                disabled={!token}
              >
                {token ? "Request To Adopt" : "Login to Adopt"}
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );  
}

export default AdoptationOffers;