import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import ProductSearchBar from "./ProductSearchBar"; // ✅ Import the new component
import {FaPaw}from"react-icons/fa";
function MangePetTypes() {
  const [petTypes, setPetTypes] = useState([]);
  const [petBreeds, setPetBreeds] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;

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
        Swal.fire("Error", "Failed to load products", "error");
      }
    };

    getPetTypes();
  }, []);
  const addPetType = () => {
    Swal.fire({
      title: "Create a Post",
      html: `
        <textarea id="swal-post-content" placeholder="Write something..." rows="4" style="width:100%; padding:10px; border-radius:5px; border:1px solid #ccc; margin-bottom:10px;"></textarea>
      `,
      showCancelButton: true,
      confirmButtonText: "Post",
      preConfirm: async () => {
        const typeName = document.getElementById("swal-post-content").value;

        if (!typeName.trim()) {
          Swal.showValidationMessage("Post content cannot be empty");
          return false;
        }

        try {

          await axios.post("https://localhost:8088/pet-types", {
            typeName:typeName
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          return true;
        } catch (error) {
          console.error("Error adding post:", error);
          Swal.showValidationMessage("Failed to add post.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Post added successfully!", "success");
      }
    });
  };
const updatePetType = (id, currentTypeName) => {
  Swal.fire({
    title: "Update Pet Type",
    html: `
      <textarea id="swal-post-content" placeholder="Write something..." rows="4"
        style="width:100%; padding:10px; border-radius:5px; border:1px solid #ccc; margin-bottom:10px;">${currentTypeName}</textarea>
    `,
    showCancelButton: true,
    confirmButtonText: "Update",
    preConfirm: async () => {
      const typeName = document.getElementById("swal-post-content").value;

      if (!typeName.trim()) {
        Swal.showValidationMessage("Pet type name cannot be empty");
        return false;
      }

      try {
        await axios.put(`https://localhost:8088/pet-types/${id}`, {
          typeName: typeName
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // ✅ Return the updated name to use in `.then()`
        return typeName;
      } catch (error) {
        console.error("Error updating pet type:", error);
        Swal.showValidationMessage("Failed to update pet type.");
        return false;
      }
    },
  }).then((result) => {
    if (result.isConfirmed && result.value) {
      Swal.fire("Success", "Pet type updated successfully!", "success");

      // ✅ Use returned value instead of accessing the DOM again
      const updatedTypeName = result.value;
      setPetTypes(prev =>
        prev.map(p => (p.id === id ? { ...p, typeName: updatedTypeName } : p))
      );
    }
  });
};
const addPetBreed = (id, currentTypeName) => {
  Swal.fire({
    title: "Add Pet Breed",
    html: `
      <textarea id="swal-post-content" placeholder="Write something..." rows="4"
        style="width:100%; padding:10px; border-radius:5px; border:1px solid #ccc; margin-bottom:10px;"></textarea>
    `,
    showCancelButton: true,
    confirmButtonText: "Add",
    preConfirm: async () => {
      const breedName = document.getElementById("swal-post-content").value;

      if (!breedName.trim()) {
        Swal.showValidationMessage("Breed name cannot be empty");
        return false;
      }

      try {
        await axios.post(`https://localhost:8088/pet-breeds`, {
          breedName: breedName,
          petTypeName: currentTypeName
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        return breedName;  // <-- Return value here
      } catch (error) {
        console.error("Error adding pet breed:", error);
        Swal.showValidationMessage("Failed to add pet breed.");
        return false;
      }
    },
  }).then((result) => {
    if (result.isConfirmed && result.value) {
      Swal.fire("Success", "Pet breed added successfully!", "success");
      // You can do something with result.value here if needed
    }
  });
};
const [expandedPetType, setExpandedPetType] = useState(null);

  // showPetBreed updated to toggle expanded pet type
  const showPetBreed = async (petTypeName) => {
    try {
      const response = await axios.get(`https://localhost:8088/pet-breeds/by-type?type=${petTypeName}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
      });
      setPetBreeds(prev => ({
        ...prev,
        [petTypeName]: response.data
      }));
      setExpandedPetType(petTypeName); // Mark this type as expanded
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to load pet breeds", "error");
    }
  };

  // Function to update a pet breed (similar to updatePetType)
  const updatePetBreed = (id, currentBreedName, petTypeName) => {
    Swal.fire({
      title: "Update Pet Breed",
      html: `
        <textarea id="swal-breed-content" placeholder="Write something..." rows="4"
          style="width:100%; padding:10px; border-radius:5px; border:1px solid #ccc; margin-bottom:10px;">${currentBreedName}</textarea>
      `,
      showCancelButton: true,
      confirmButtonText: "Update",
      preConfirm: async () => {
        const breedName = document.getElementById("swal-breed-content").value;
        if (!breedName.trim()) {
          Swal.showValidationMessage("Breed name cannot be empty");
          return false;
        }
        try {
          await axios.put(`https://localhost:8088/pet-breeds/${id}`, {
            breedName: breedName,
            petTypeName:petTypeName
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          return breedName;
        } catch (error) {
          console.error("Error updating pet breed:", error);
          Swal.showValidationMessage("Failed to update pet breed.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        Swal.fire("Success", "Pet breed updated successfully!", "success");
        const updatedName = result.value;
        // Update breed name in state
        setPetBreeds(prev => ({
          ...prev,
          [petTypeName]: prev[petTypeName].map(b => b.id === id ? { ...b, breedName: updatedName } : b)
        }));
      }
    });
  };

  // Function to delete pet breed
  const deletePetBreed = async (id, petTypeName) => {
    try {
      await axios.delete(`https://localhost:8088/pet-breeds/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      Swal.fire("Deleted", "Pet breed removed", "success");
      setPetBreeds(prev => ({
        ...prev,
        [petTypeName]: prev[petTypeName].filter(b => b.id !== id)
      }));
    } catch (error) {
      Swal.fire("Error", "Failed to delete pet breed", "error");
    }
  };
return (
    <div className="container p-4">
      <h2 className="mb-4">Products</h2>

      <button onClick={addPetType} className="btn btn-primary mb-3">Add Pet Type</button>

      <table className="table text-center">
        <thead className="thead-light">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Pet Breeds</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {petTypes.length > 0 ? petTypes.map(petType => (
            <tr key={petType.id}>
              <td>{petType.id}</td>
              <td>{petType.typeName}</td>
              <td>
                {expandedPetType === petType.typeName ? (
                  // Show pet breeds with Update/Delete buttons
                  <ul style={{ textAlign: "left", marginTop: "10px" }}>
                    {petBreeds[petType.typeName]?.map(breed => (
                      <li key={breed.id} style={{ marginBottom: "8px" }}>
                        {breed.breedName}
                        <button
                          className="btn btn-warning btn-sm mx-2"
                          onClick={() => updatePetBreed(breed.id, breed.breedName, petType.typeName)}
                        >
                          Update
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => deletePetBreed(breed.id, petType.typeName)}
                        >
                          Delete
                        </button>
                      </li>
                    ))}
                  </ul>
                ) : (
                  // Show "Show Pet Breeds" button only if not expanded
                  <button onClick={() => showPetBreed(petType.typeName)}>Show Pet Breeds</button>
                )}
              </td>
              <td>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={async () => {
                    try {
                      await axios.delete(`https://localhost:8088/pet-types/${petType.id}`, {
                        headers: {
                          Authorization: `Bearer ${token}`,
                          'Content-Type': 'application/json'
                        }
                      });
                      Swal.fire("Deleted", "Product removed", "success");
                      setPetTypes(prev => prev.filter(p => p.id !== petType.id));
                      if (expandedPetType === petType.typeName) setExpandedPetType(null);
                    } catch (error) {
                      Swal.fire("Error", "Unauthorized or failed", "error");
                    }
                  }}
                >
                  Remove
                </button>
                <button className="btn btn-warning btn-sm mx-2" onClick={() => updatePetType(petType.id, petType.typeName)}>Update</button>
                <button className="btn btn-warning btn-sm mx-2" onClick={() => addPetBreed(petType.id, petType.typeName)}>Add Pet Breed</button>
              </td>
            </tr>
          )) : (
                      <td colSpan="5" className="text-center py-5">
                        <div className="text-muted">
                          <FaPaw className="display-5 mb-3" />
                          <h4>No items found</h4>
                        </div>
                      </td>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default MangePetTypes;
