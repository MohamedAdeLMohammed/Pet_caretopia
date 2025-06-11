import { useEffect, useState } from "react";
import axios from 'axios';
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
function AdoptationOffers() {
  const [pets, setPets] = useState([]);
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const requestAdoption = (petId) => {
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
      window.location.reload();
    }
  });
};

  useEffect(() => {
    const getPetsOffers = async () => {
      try {
        const response = await axios.get('https://localhost:8088/adoptions/available-for-adoption', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setPets(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    getPetsOffers();
  }, [token]);

  // Filter out pets owned by the user
  const availablePets = pets.filter(pet => pet.ownerId !== userId);

return (
  <>
    <div className="store-management-container">
      <h2 className="management-dashboard-title">Adoption Offers</h2>
      {availablePets.length === 0 ? (
        <p>No pets available for adoption.</p>
      ) : (
        <div className="management-grid">
          {availablePets.map((pet) => (
            <div className="management-card" key={pet.petID}>
              <img src={pet.imageUrl || "https://via.placeholder.com/150"} alt={pet.petName} />
              <p className="management-card-title">{pet.petName}</p>
              <p>{pet.petTypeName}</p>
              <p>{pet.petBreedName}</p>
              <button 
                className='management-card-button' 
                onClick={() => {
                  requestAdoption(pet.petID);
                }}
              >
                Request To Adopt
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  </>
);  
}

export default AdoptationOffers;
