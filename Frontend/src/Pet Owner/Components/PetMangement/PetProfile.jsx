import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";
import "../../CSS/PTODashboard.css";
function PetProfile() {
  const { petID } = useParams();
  const [pet, setPet] = useState(null);
  const [vaccines, setVaccines] = useState([]);
  const [petVaccines,setPetVaccines] = useState([]);
  const token = sessionStorage.getItem("token");
  const getPet = async () => {
    try {
      const response = await axios.get(`https://localhost:8088/pets/${petID}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPet(response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
      Swal.fire("Error", "Failed to fetch pet profile.", "error");
    }
  };

  const getVaccines = async () => {
    try {
      const response = await axios.get(`https://localhost:8088/vaccines/all`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setVaccines(response.data);
      console.log(response.data)
    } catch (error) {
      console.error("Error fetching vaccines:", error);
      Swal.fire("Error", "Failed to fetch vaccines.", "error");
    }
  };
    const getPetVaccines = async () => {
    try {
      const response = await axios.get(`https://localhost:8088/petVaccines/pet/${petID}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPetVaccines(response.data);
      console.log(response.data)
    } catch (error) {
      console.error("Error fetching vaccines:", error);
      Swal.fire("Error", "Failed to fetch vaccines.", "error");
    }
  };

const addVaccine = async () => {
  // Load vaccines first if they're not already loaded
  if (vaccines.length === 0) {
    await getVaccines();
  }

  const vaccineOptions = vaccines.map(v => 
    `<option value="${v.vaccineId}">${v.vaccineName}</option>`
  ).join("");

  Swal.fire({
    title: "Add Vaccine",
    html: `
            Vaccination Date
      <input type="date" id="vaccinationDate" class="swal2-input" placeholder="Vaccine Date" />
      <br>
            Next Dose Due

      <input type="date" id="nextDoseDue" class="swal2-input" placeholder="Next Dose Due" />
      <input type="text" id="notes" class="swal2-input" placeholder="Notes" />
      <select id="vaccineType" class="swal2-input">
              <option value="">Select a vaccine</option>
        ${vaccineOptions}
      </select>
    `,
    showCancelButton: true,
    confirmButtonText: "Add",
    preConfirm: async () => {
      const vaccineTypeId = document.getElementById("vaccineType").value;
      const vaccinationDate = document.getElementById("vaccinationDate").value.trim();
      const nextDoseDue = document.getElementById("nextDoseDue").value.trim();
      const notes = document.getElementById("notes").value.trim();

      if (!vaccinationDate || !nextDoseDue || !vaccineTypeId) {
        Swal.showValidationMessage("All fields are required.");
        return false;
      }

      try {
        await axios.post(
          `https://localhost:8088/petVaccines/add/pet/${petID}`,
          {
            vaccinationDate,
            nextDoseDue,
            notes,
            petVaccine: { vaccineId: vaccineTypeId },
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        return true;
      } catch (error) {
        console.error("Error adding vaccine:", error);
        Swal.showValidationMessage("Failed to add vaccine.");
        return false;
      }
    },
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire("Success", "Vaccine added successfully!", "success");
      getPetVaccines(); // Refresh the vaccine list
    }
  });
};

  useEffect(() => {
    getPet();
    getPetVaccines();
    getVaccines();
  }, []);

  if (!pet) return <p>Loading pet profile...</p>;

  return (
    
  <div className="pet-profile-container">
  <h2  className="management-dashboard-title">Pet Profile</h2>
    <div className="pet-info-section">
      <div className="pet-details">
        <p><strong>Name:</strong> {pet.petName}</p>
        <p><strong>Type:</strong> {pet.petTypeName}</p>
        <p><strong>Breed:</strong> {pet.petBreedName}</p>
        <p><strong>Breed:</strong> {pet.gender}</p>
      </div>
      <div className="pet-image-container">
        <img src={pet.imageUrl} alt={pet.petName} className="pet-image" />
</div>
      <div className="col-12 mt-4">
        <h2 className="text-center">Health Section</h2>
        <button className="btn btn-primary" onClick={()=>{addVaccine()}}>Add Vaccine</button>
        <div className="accordion mt-4" id="accordionExample">
  {petVaccines.length === 0 ? (
    <p className="text-center">No vaccines recorded for this pet.</p>
  ) : (
    petVaccines.map((data, index) => (
      <div className="accordion-item" key={index}>
        <h2 className="accordion-header" id={`heading-${index}`}>
          <button
            className={`accordion-button ${index !== 0 ? "collapsed" : ""}`}
            type="button"
            data-bs-toggle="collapse"
            data-bs-target={`#collapse-${index}`}
            aria-expanded={index === 0 ? "true" : "false"}
            aria-controls={`collapse-${index}`}
          >
            Vaccine #{index + 1} - {data.petVaccine.vaccineName}
          </button>
        </h2>
        <div
          id={`collapse-${index}`}
          className={`accordion-collapse collapse ${index === 0 ? "show" : ""}`}
          aria-labelledby={`heading-${index}`}
          data-bs-parent="#accordionExample"
        >
          <div className="accordion-body">
            <ul>
              <li><strong>Name:</strong> {data.petVaccine.vaccineName}</li>
              <li><strong>Description:</strong> {data.petVaccine.description}</li>
              <li><strong>Vaccination Date:</strong> {data.vaccinationDate}</li>
              <li><strong>Next Dose Due:</strong> {data.nextDoseDue}</li>
              <li><strong>Notes:</strong> {data.notes}</li>
            </ul>
          </div>
        </div>
      </div>
          ))
  )}
</div>
</div>
      </div>
            </div>

  );
}

export default PetProfile;
