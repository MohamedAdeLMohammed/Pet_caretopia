import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
import { useParams, useNavigate } from "react-router-dom";

function UpdatePet() {
  const [petName, setPetName] = useState("");
  const [petTypeId, setPetTypeId] = useState("");
const [petTypeName, setPetTypeName] = useState(""); // changed to ID
  const [petBreedName, setPetBreedName] = useState("");
  const [petTypes, setPetTypes] = useState([]);
  const [petBreedId, setPetBreedId] = useState("");
  const [petBreeds, setPetBreeds] = useState([]);
  const [petAge, setPetAge] = useState("");
  const [petGender, setPetGender] = useState("");
  const [imageFiles, setImageFiles] = useState([]);

  const token = sessionStorage.getItem("token");
  const { shelterId, petID } = useParams();
  const decode = jwtDecode(token);
  const navigate = useNavigate();

  // Fetch pet and pet types on mount
  useEffect(() => {
    const getPet = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/pets/${petID}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        const pet = response.data;
        console.log(pet)
        setPetName(pet.petName);
        setPetGender(pet.gender);
        setPetAge(pet.age);
        setPetBreedName(pet.petBreedName);
        setPetTypeName(pet.petTypeName);
      } catch (error) {
        console.error("Error fetching pet:", error.response?.data || error.message);
        Swal.fire("Error", "Could not load pet data", "error");
      }
    };

    const getPetTypes = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/pet-types`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setPetTypes(response.data);
      } catch (error) {
        console.error("Error loading pet types:", error.response?.data || error.message);
        Swal.fire("Error", "Failed to load pet types", "error");
      }
    };

    getPetTypes();
    getPet();
  }, [token, petID]);

  // Fetch pet breeds when type changes
  useEffect(() => {
    const getPetBreeds = async () => {
      if (!petTypeId) return;
      try {
        const response = await axios.get(
          `https://localhost:8088/pet-breeds/by-type-id?typeId=${petTypeId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        setPetBreeds(response.data);
      } catch (error) {
        console.error("Error loading pet breeds:", error.response?.data || error.message);
        Swal.fire("Error", "Failed to load pet breeds", "error");
      }
    };

    getPetBreeds();
  }, [petTypeId, token]);

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const updatePet = async () => {
    if (!token) {
      Swal.fire("Unauthorized", "You must be logged in", "error");
      return;
    }

    try {
      const pet = {
        petName,
        petTypeId,
        gender: petGender,
        age: petAge,
        petBreedId,
      };

      const formData = new FormData();
      formData.append("pet", new Blob([JSON.stringify(pet)], { type: "application/json" }));
      imageFiles.forEach((file) => {
        formData.append("images", file);
      });

      const response = await axios.put(`https://localhost:8088/pets/${petId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        icon: "success",
        title: "Pet updated successfully",
        text: `Pet ID: ${response.data.id}`,
      });

      if (decode.role === "ADMIN") {
        navigate(`/dashboard/sheltersMangement/shelter/${shelterId}`);
      } else {
        navigate("/dashboard/pets");
      }

    } catch (error) {
      console.error("Error updating pet:", error.response?.data || error.message);
      Swal.fire({
        icon: "error",
        title: "Failed to update pet",
        text: error.response?.data?.message || error.message || "An error occurred",
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    updatePet();
  };

  return (
    <div className="p-3 container mt-5 mb-5">
      <form onSubmit={handleFormSubmit} className="post-form">
        <div className="container row g-3">
          <div className="col-md-12">
            <label className="form-label">Pet Name</label>
            <input
              type="text"
              className="form-control"
              value={petName}
              onChange={(e) => setPetName(e.target.value)}
              required
            />
          </div>

          <div className="col-md-12">
            <label className="form-label">Pet Age</label>
            <input
              type="text"
              className="form-control"
              value={petAge}
              onChange={(e) => setPetAge(e.target.value)}
              required
            />
          </div>

          <div className="col-md-12">
            <label className="form-label">Pet Gender</label>
            <select
              className="form-control"
              value={petGender}
              onChange={(e) => setPetGender(e.target.value)}
              required
            >
              <option value="">Select Gender...</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div className="col-md-6">
            <label className="form-label">Pet Type</label>
            <select
              className="form-select"
              value={petTypeName}
              onChange={(e) => {
                setPetTypeId(e.target.value);
                setPetBreedId("");
              }}
              required
            >
              <option value="">Select a type</option>
              {petTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {type.typeName}
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-6">
            <label className="form-label">Pet Breed</label>
            <select
              className="form-select"
              value={petBreedName}
              onChange={(e) => setPetBreedName(e.target.value)}
              required
              disabled={!petBreeds.length}
            >
              <option value="">Select a breed</option>
              {petBreeds.map((breed) => (
                <option key={breed.id} value={breed.id}>
                  {breed.breedName}
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-12">
            <label className="form-label">Upload Images</label>
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
              Update Pet
            </button>
            <button
              type="reset"
              className="btn btn-danger"
              onClick={() => {
                setPetName("");
                setPetTypeId("");
                setPetBreedId("");
                setPetAge("");
                setPetGender("");
                setImageFiles([]);
                setPetBreeds([]);
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

export default UpdatePet;
