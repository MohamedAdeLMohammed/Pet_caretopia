import { useEffect, useState } from "react";
import axios from 'axios';
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { FaPlusCircle, FaEye, FaEdit, FaHandshake, FaTrash } from "react-icons/fa";
import "../../CSS/PTODashboard.css";

function PetsMangement() {
  const [pets, setPets] = useState([]);
  const token = sessionStorage.getItem('token');

  useEffect(() => {
    const getOwnerPets = async () => {
      try {
        const response = await axios.get('https://localhost:8088/pets/mine', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setPets(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    getOwnerPets();
  }, []);

  return (
    <>
      <div className="store-management-container">
        <h2 className="management-dashboard-title">My Pets</h2>
        <Link to={'addPet'} className="management-card-button" style={{ marginRight: '5px' }}>
          <FaPlusCircle /> Add Pet
        </Link>

        <div className="container row gap-5 pt-3 pb-3 ms-2 pets-cards">
          <div className="all-cats-container">
            <div className="product-grid">
              {pets.map((pet) => (
                <div className="product-card" key={pet.petID}>
                  <img
                    src={pet.imageUrl || 'https://via.placeholder.com/150'}
                    alt={pet.petName}
                    className="product-image"
                  />
                  <h4 className="management-card-title" style={{ fontSize: '1.25rem', fontWeight: '600', marginBottom: '0.5rem' }}>
                    {pet.petName}
                  </h4>
                  <p style={{ margin: '0.25rem 0', color: '#555', fontSize: '0.9rem' }}>Pet Type: {pet.petTypeName}</p>
                  <h5 style={{ margin: '0.5rem 0', fontSize: '1rem', fontWeight: '500' }}>Breed: {pet.petBreedName}</h5>
                  <h5 style={{ margin: '0.5rem 0', fontSize: '1rem', fontWeight: '500' }}>Gender: {pet.gender}</h5>

                  <Link to={`updatePet/${pet.petID}`} className="editButton">
                    <FaEdit style={{ fontSize: '16px' }} /> Edit Pet
                  </Link>

                  <Link to={`petProfile/${pet.petID}`} className="viewButton">
                    <FaEye style={{ fontSize: '16px' }} /> View Profile
                  </Link>

                  <button
                    className="offerButton"
                    style={{ marginTop: "10px" }}
                    onClick={async () => {
                      try {
                        await axios.put(
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
                    }}
                  >
                    <FaHandshake style={{ fontSize: '16px' }} /> Offer To Adopt
                  </button>

                  <button
                    className="offerButton"
                    style={{ marginTop: "10px" }}
                    onClick={async () => {
                      try {
                        await axios.put(
                          `https://localhost:8088/breeding-requests/${pet.petID}/make-available-for-breeding`,
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
                    }}
                  >
                    <FaHandshake style={{ fontSize: '16px' }} /> Offer To Breed
                  </button>

                  <button
                    className="offerButton"
                    style={{ marginTop: "10px" }}
                    onClick={async () => {
                      try {
                        await axios.delete(`https://localhost:8088/pets/${pet.petID}`, {
                          headers: {
                            Authorization: `Bearer ${token}`,
                            'Content-Type': 'application/json'
                          }
                        });
                        Swal.fire("Deleted", "Pet removed", "success");
                        // Remove pet from state without refresh
                        setPets(prev => prev.filter(p => p.petID !== pet.petID));
                      } catch (error) {
                        Swal.fire("Error", "Failed to delete", "error");
                      }
                    }}
                  >
                    <FaTrash style={{ marginRight: "5px" }} /> Remove
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
