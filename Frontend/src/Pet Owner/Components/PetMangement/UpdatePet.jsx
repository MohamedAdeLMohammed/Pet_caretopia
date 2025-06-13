import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
import { useParams, useNavigate } from "react-router-dom";

function UpdatePet() {
  const [petName, setPetName] = useState("");
  const [petTypeId, setPetTypeId] = useState("");
  const [petTypeName, setPetTypeName] = useState("");
  const [petBreedId, setPetBreedId] = useState("");
  const [petBreedName, setPetBreedName] = useState("");
  const [petTypes, setPetTypes] = useState([]);
  const [petBreeds, setPetBreeds] = useState([]);
  const [petAge, setPetAge] = useState("");
  const [petGender, setPetGender] = useState("");
  const [imageFiles, setImageFiles] = useState([]);
  const [existingImages, setExistingImages] = useState([]);

  const token = sessionStorage.getItem("token");
  const { shelterId, petID } = useParams();
  const decode = jwtDecode(token);
  const navigate = useNavigate();

  useEffect(() => {
    const getPet = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/pets/${petID}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const pet = response.data;
        setPetName(pet.petName);
        setPetGender(pet.gender);
        setPetAge(pet.age);
        setPetBreedName(pet.petBreedName);
        setPetBreedId(pet.petBreedId);
        setPetTypeName(pet.petTypeName);
        setPetTypeId(pet.petTypeId);
        setExistingImages(pet.images || []);
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

  useEffect(() => {
    const getPetBreeds = async () => {
      if (!petTypeName) return;
      try {
        const response = await axios.get(
          `https://localhost:8088/pet-breeds/by-type?type=${petTypeName}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
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
  }, [petTypeName, token]);

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

      const response = await axios.put(`https://localhost:8088/pets/${petID}`, formData, {
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
        text: error.response?.data?.message || "An error occurred",
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
            />
          </div>

          <div className="col-md-12">
            <label className="form-label">Pet Age</label>
            <input
              type="number"
              className="form-control"
              value={petAge}
              onChange={(e) => setPetAge(e.target.value)}
            />
          </div>

          <div className="col-md-12">
            <label className="form-label">Pet Gender</label>
            <select
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
            <label className="form-label">Current Images</label>
            <div className="d-flex flex-wrap gap-2 mb-3">
              {existingImages.map((image, index) => (
                <img
                  key={index}
                  src={image}
                  alt={`Pet ${index}`}
                  className="img-thumbnail"
                  style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                />
              ))}
            </div>
          </div>

          <div className="col-md-12">
            <label className="form-label">Upload New Images</label>
            <input
              type="file"
              className="form-control"
              multiple
              accept="image/*"
              onChange={handleImageChange}
            />
            <small className="text-muted">Select new images to replace existing ones</small>
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
                setPetBreedName("");
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
