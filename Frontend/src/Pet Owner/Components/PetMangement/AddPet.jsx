import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
import { useParams } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
function AddPet() {
  const [petName, setPetName] = useState("");
  const [petTypeName, setPetTypeName] = useState(""); // changed to ID
  const [petTypes, setPetTypes] = useState([]);
  const [petBreedName, setPetBreedName] = useState("");
    const [petTypeId, setPetTypeId] = useState("");
  const [petBreeds, setPetBreeds] = useState([]);
  const [petAge, setPetAge] = useState("");
  const [petGender, setPetGender] = useState("");
  const [imageFiles, setImageFiles] = useState([]);
  const token = sessionStorage.getItem("token");
  const { shelterId } = useParams();
  const decode = jwtDecode(token);
    const navigate = useNavigate();
  // Fetch all pet types on mount
  useEffect(() => {
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
        console.error(error);
        Swal.fire("Error", "Failed to load pet types", "error");
      }
    };

    getPetTypes();
  }, [token]);

  // Fetch pet breeds whenever pet type changes
  useEffect(() => {
    const getPetBreeds = async () => {
      if (!petTypeName) return;
      try {
        const response = await axios.get(
          `https://localhost:8088/pet-breeds/by-type?type=${petTypeName}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        setPetBreeds(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load pet breeds", "error");
      }
    };

    getPetBreeds();
  }, [petTypeName, token]);

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const addPet = async () => {
    if (!token) {
      Swal.fire("Unauthorized", "You must be logged in", "error");
      return;
    }

    try {
      const pet = {
        petName,
        petTypeName,
        gender: petGender,
        age: petAge,
        petBreedName,
      };

      if (decode.role === "ADMIN") {
        pet.shelterId = shelterId;
      }

      const formData = new FormData();
      formData.append("pet", new Blob([JSON.stringify(pet)], { type: "application/json" }));
      imageFiles.forEach((file) => {
        formData.append("image", file);
      });

      const response = await axios.post("https://localhost:8088/pets", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        icon: "success",
        title: "Pet added successfully",
        text: `Pet ID: ${response.data.id}`,
      });
      if(decode.role === "ADMIN"){
        navigate(`/dashboard/sheltersMangement/shelter/${shelterId}`)
      }else{
        navigate("/dashboard/pets")
      }
      // Reset fields
      setPetName("");
      setPetTypeId("");
      setPetBreedName("");
      setPetAge("");
      setPetGender("");
      setImageFiles([]);
      setPetBreeds([]);

    } catch (error) {
      console.error("Error adding pet:", error);
      Swal.fire({
        icon: "error",
        title: "Failed to add pet",
        text: error.response?.data?.message || "An error occurred",
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    addPet();
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
  id="selectedGender"
  className="form-control"
  value={petGender}
  onChange={(e) => setPetGender(e.target.value)}
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
    setPetTypeName(e.target.value); // this will now be the typeName
    setPetBreedName(""); // reset breed
  }}
  required
>
  <option value="">Select a type</option>
  {petTypes.map((type) => (
    <option key={type.id} value={type.typeName}>
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
                <option key={breed.id} value={breed.breedName}>
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
              Add Pet
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

export default AddPet;
