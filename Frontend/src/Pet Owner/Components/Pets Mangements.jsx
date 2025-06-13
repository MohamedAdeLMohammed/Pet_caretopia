import { useEffect, useState } from "react";
import cat from '../../assets/cat 1.png'
import axios from 'axios';
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { FaPlusCircle,FaEye,FaEdit ,FaHandshake} from "react-icons/fa";
import "../CSS/PTODashboard.css";
function PetsMangement(){
    const [pets,setPets] = useState([]);
    const token = sessionStorage.getItem('token');
useEffect(() => {
    const getOwnerPets = async () => {
        try {
            const response = await axios.get('https://localhost:8088/pets/mine',{
              headers: {
              Authorization: `Bearer ${token}`,
            },
            });
            console.log(response.data);
            setPets(response.data)  // This logs the product details
            // You can set state here, for example:
            // setPets(response.data.vaccines.$values)
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    getOwnerPets();
    }, []);
      const addVaccine = (vaccineTypeId, vaccineTypeName) => {
    Swal.fire({
      title: "Add Vaccine",
      html: `
        <input type="text" id="vaccine-name" class="swal2-input" placeholder="Vaccine Name" />
        <input type="text" id="vaccine-description" class="swal2-input" placeholder="Description" />
        <input type="number" id="recommended-age" class="swal2-input" placeholder="Recommended Age (Weeks)" />
      `,
      showCancelButton: true,
      confirmButtonText: "Add",
      preConfirm: async () => {
        const name = document.getElementById("vaccine-name").value.trim();
        const description = document.getElementById("vaccine-description").value.trim();
        const age = document.getElementById("recommended-age").value.trim();

        if (!name) {
          Swal.showValidationMessage("Vaccine name cannot be empty");
          return false;
        }

        try {
          await axios.post(`https://localhost:8088/vaccines/add`, {
            vaccineName: name,
            description,
            recommendedAgeWeeks: age,
            vaccineType: { id: vaccineTypeId }
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          return name;
        } catch (error) {
          console.error("Error adding vaccine:", error);
          Swal.showValidationMessage("Failed to add vaccine.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Vaccine added successfully!", "success");
        showVaccines(vaccineTypeName); // Refresh list
      }
    });
  };
    return(
        <>
                <div className="store-management-container">

                <h2  className="management-dashboard-title">My Pets</h2>
<Link to={'addPet'} className="management-card-button" style={{marginRight: '5px'}}>
  <FaPlusCircle /> Add Pet
</Link>
        <div className=" container row gap-5 pt-3 pb-3 ms-2 pets-cards">
                  <div className="all-cats-container">
        <div className="product-grid">
      {pets.map((pet) => (
          <div className="product-card" key={pet.petID}>
            <img
              src={pet.imageUrl || 'https://via.placeholder.com/150'}
              alt={pet.petName}
              className="product-image"
            />
<h4 className="management-card-title" style={{fontSize: '1.25rem', fontWeight: '600', marginBottom: '0.5rem'}}>{pet.petName}</h4>
<p style={{margin: '0.25rem 0', color: '#555', fontSize: '0.9rem'}}>Pet Type : {pet.petTypeName}</p>
<h5 style={{margin: '0.5rem 0', fontSize: '1rem', fontWeight: '500'}}>Breed : {pet.petBreedName}</h5>         
<Link to={`updatePet/${pet.petID}`} className="editButton" >
  <FaEdit style={{ fontSize: '16px' }} />
  Edit Pet
</Link>
<Link to={`petProfile/${pet.petID}`} className="viewButton">
  <FaEye style={{ fontSize: '16px' }} />
  View Profile
</Link>
<button 
 className="offerButton"
 style={{marginTop:"10px"}}
  onClick={async () => {
    try {
      const response = await axios.put(
        `https://localhost:8088/pets/${pet.petID}/offer-for-adoption`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added Successfully",
        showConfirmButton: false,
        timer: 1500
      });
    } catch (error) {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Unauthorized: Please log in again",
        showConfirmButton: false,
        timer: 1500
      });
      console.error(error);
    }
  }}>
  <FaHandshake style={{ fontSize: '16px' }} />
  Offer To Adopt
  </button>
  
          </div>
        ))}
        </div>
      </div>
        </div>
        </div>
        </>
    );
}
export default PetsMangement;