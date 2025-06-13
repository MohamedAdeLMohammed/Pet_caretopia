import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { jwtDecode } from "jwt-decode";

function BreedingOffers() {
  const [pets, setPets] = useState([]);
  const [offers, setOffers] = useState([]);
  const token = sessionStorage.getItem("token");
  const decode = jwtDecode(token);
  const userId = decode.id;

  const getBreedingOffers = async () => {
    try {
      const response = await axios.get(
        "https://localhost:8088/breeding-requests/available-for-breeding",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Breeding Offers:", response.data);
      setOffers(response.data);
    } catch (error) {
      console.error("Error fetching breeding offers:", error);
    }
  };

  const getOwnerPets = async () => {
    try {
      const response = await axios.get("https://localhost:8088/pets/mine", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Owner Pets:", response.data);
      setPets(response.data);
    } catch (error) {
      console.error("Error fetching pets:", error);
    }
  };

  const requestBreeding = async (offerPetId) => {
    if (pets.length === 0) {
      await getOwnerPets();
    }

    const petsOptions = pets
      .map((v) => `<option value="${v.petID}">${v.petName}</option>`)
      .join("");

    Swal.fire({
      title: "Send Breeding Request",
      html: `
        <select id="pet" class="swal2-input">
          <option value="">Select Your Pet</option>
          ${petsOptions}
        </select>
      `,
      showCancelButton: true,
      confirmButtonText: "Send Request",
      preConfirm: async () => {
        const selectedPetId = Number(document.getElementById("pet").value);

        if (!selectedPetId) {
          Swal.showValidationMessage("Please select your pet.");
          return false;
        }

        try {
          const selectedPet = pets.find((p) => p.petID === selectedPetId);
          const offerPet = offers.find((p) => p.petID === offerPetId);

          if (!selectedPet || !offerPet) {
            Swal.showValidationMessage("Pet information not found.");
            return false;
          }

          // Determine which one is male/female
          let malePetId, femalePetId;

          if (selectedPet.gender === "MALE" && offerPet.gender === "FEMALE") {
            malePetId = selectedPet.petID;
            femalePetId = offerPet.petID;
          } else if (
            selectedPet.gender === "FEMALE" &&
            offerPet.gender === "MALE"
          ) {
            malePetId = offerPet.petID;
            femalePetId = selectedPet.petID;
          } else {
            Swal.showValidationMessage(
              "Breeding can only be requested between a male and a female pet."
            );
            return false;
          }
          console.log(malePetId)
          console.log(femalePetId)
          await axios.post(
            "https://localhost:8088/breeding-requests",
            {
              malePetId,
              femalePetId,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          return true;
        } catch (error) {
            console.error("Error sending breeding request:", error);
            const errorMessage = error.response?.data?.message || "An unexpected error occurred.";
            Swal.showValidationMessage(errorMessage);
            return false;
          }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Breeding request sent successfully!", "success");
        window.location.reload();
      }
    });
  };

  useEffect(() => {
    getBreedingOffers();
    getOwnerPets();
  }, []);

  const availablePets = offers.filter((pet) => pet.ownerId !== userId);

  return (
    <div className="store-management-container">
      <h2 className="management-dashboard-title">Breeding Offers</h2>
      {availablePets.length === 0 ? (
        <p>No pets available for breeding.</p>
      ) : (
        <div className="management-grid">
          {availablePets.map((pet) => (
            <div className="management-card" key={pet.petID}>
              <img
                src={pet.imageUrl || "https://via.placeholder.com/150"}
                alt={pet.petName}
              />
              <p className="management-card-title">{pet.petName}</p>
              <p>{pet.petTypeName}</p>
              <p>{pet.petBreedName}</p>
              <p>{pet.gender}</p>
              <button
                className="management-card-button"
                onClick={() => requestBreeding(pet.petID)}
              >
                Request Breeding
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default BreedingOffers;
