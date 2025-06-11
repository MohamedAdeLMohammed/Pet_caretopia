import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import ProductSearchBar from "../StoreMangement/ProductSearchBar";
import "../../CSS/AdminDashboard.css";
import { FaPaw,FaTrash, FaEdit, FaPlus, FaEye} from 'react-icons/fa';
function Vaccines() {
  const [vaccineTypes, setVaccineTypes] = useState([]);
  const [vaccines, setVaccines] = useState({});
  const [expandedVaccineType, setExpandedVaccineType] = useState(null);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const userId = jwtDecode(token).id;

  useEffect(() => {
    const fetchVaccineTypes = async () => {
      try {
        const response = await axios.get("https://localhost:8088/vaccineTypes/all", {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        console.log(response.data)
        setVaccineTypes(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load vaccine types", "error");
      }
    };

    fetchVaccineTypes();
  }, []);

  const addVaccineType = () => {
    Swal.fire({
      title: "Add Vaccine Type",
      html: `
        <input id="swal-vaccine-type-name" class="swal2-input" placeholder="Vaccine Type Name" />
        <input id="swal-vaccine-type-description" class="swal2-input" placeholder="Description" />
      `,
      showCancelButton: true,
      confirmButtonText: "Add",
       confirmButtonColor: "#023C5A", 
      preConfirm: async () => {
        const name = document.getElementById("swal-vaccine-type-name").value.trim();
        const description = document.getElementById("swal-vaccine-type-description").value.trim();

        if (!name) {
          Swal.showValidationMessage("Vaccine type name cannot be empty");
          return false;
        }

        try {
          await axios.post("https://localhost:8088/vaccineTypes/add", {
            name,
            description
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          return true;
        } catch (error) {
          console.error("Error adding vaccine type:", error);
          Swal.showValidationMessage("Failed to add vaccine type.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Vaccine type added successfully!", "success");
        window.location.reload(); // Refresh list
      }
    });
  };

  const updateVaccineType = (id, currentName, currentDescription) => {
    Swal.fire({
  title: "Update Vaccine Type",
  html: `
    <input id="swal-vaccine-type-name" class="swal2-input" placeholder="Vaccine Type Name" value="${currentName}" />
    <input id="swal-vaccine-type-description" class="swal2-input" placeholder="Description" value="${currentDescription || ''}" />
  `,
    showCancelButton: true,
    confirmButtonText: "Update",
       confirmButtonColor: "#023C5A", 
       cancelButtonColor: "#6c757d",
  preConfirm: async () => {
    const name = document.getElementById("swal-vaccine-type-name").value.trim();
    const description = document.getElementById("swal-vaccine-type-description").value.trim();

    if (!name) {
      Swal.showValidationMessage("Vaccine type name cannot be empty");
      return false;
    }

    try {
      await axios.put(`https://localhost:8088/vaccineTypes/vaccineType/${id}`, {
        name,
        description
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      return { name, description }; // ✅ Return both
    } catch (error) {
      console.error("Error updating vaccine type:", error);
      Swal.showValidationMessage("Failed to update vaccine type.");
      return false;
    }
  },
}).then((result) => {
  if (result.isConfirmed && result.value) {
    const { name, description } = result.value; // ✅ Destructure the returned object
    Swal.fire("Success", "Vaccine type updated successfully!", "success");

    setVaccineTypes(prev =>
      prev.map(v =>
        v.vaccineTypeId === id
          ? { ...v, vaccineTypeName: name, vaccineTypeDescription: description }
          : v
      )
    );
  }
});

      }

  ;

  const deleteVaccineType = async (id) => {
    try {
      await axios.delete(`https://localhost:8088/vaccineTypes/vaccineType/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      Swal.fire("Deleted", "Vaccine type removed", "success");
      setVaccineTypes(prev => prev.filter(v => v.vaccineTypeId !== id));
      setExpandedVaccineType(null);
    } catch (error) {
      Swal.fire("Error", "Failed to delete vaccine type", "error");
    }
  };

  const showVaccines = async (typeName) => {
    try {
      const response = await axios.get(`https://localhost:8088/vaccines/type?vaccineTypeName=${typeName}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
      });
      setVaccines(prev => ({
        ...prev,
        [typeName]: response.data
      }));
      setExpandedVaccineType(typeName);
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to load vaccines", "error");
    }
  };

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
       confirmButtonColor: "#023C5A", 
       cancelButtonColor: "#6c757d",
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

  const updateVaccine = (vaccineId, currentName, currentTypeName, currentDescription, currentAge) => {
    Swal.fire({
      title: "Update Vaccine",
      html: `
        <input type="text" id="vaccine-name" class="swal2-input" placeholder="Vaccine Name" value="${currentName}" />
        <input type="text" id="vaccine-description" class="swal2-input" placeholder="Description" value="${currentDescription || ''}" />
        <input type="number" id="recommended-age" class="swal2-input" placeholder="Recommended Age (Weeks)" value="${currentAge || ''}" />
      `,
       showCancelButton: true,
       confirmButtonText: "Update",
       confirmButtonColor: "#023C5A", 
       cancelButtonColor: "#6c757d",
      preConfirm: async () => {
        const name = document.getElementById("vaccine-name").value.trim();
        const description = document.getElementById("vaccine-description").value.trim();
        const age = document.getElementById("recommended-age").value.trim();

        if (!name) {
          Swal.showValidationMessage("Vaccine name cannot be empty");
          return false;
        }

        try {
          await axios.put(`https://localhost:8088/vaccines/vaccine/${vaccineId}`, {
            vaccineName: name,
            description,
            recommendedAgeWeeks: age
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          return name;
        } catch (error) {
          console.error("Error updating vaccine:", error);
          Swal.showValidationMessage("Failed to update vaccine.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        Swal.fire("Success", "Vaccine updated successfully!", "success");
        showVaccines(currentTypeName);
      }
    });
  };

  const deleteVaccine = async (vaccineId, vaccineTypeName) => {
    try {
      await axios.delete(`https://localhost:8088/vaccines/vaccine/${vaccineId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      Swal.fire("Deleted", "Vaccine removed", "success");
      setVaccines(prev => ({
        ...prev,
        [vaccineTypeName]: prev[vaccineTypeName].filter(v => v.vaccineId !== vaccineId)
      }));
    } catch (error) {
      Swal.fire("Error", "Failed to delete vaccine", "error");
    }
  };

  return (
   <div className="products-management-container">
    <div className="products container">
      <h2 className="management-dashboard-title">Vaccines Management</h2>

      <button onClick={addVaccineType} className="Add-btn">Add Vaccine Type</button>

         <div className="table-responsive">
          <table className="table products-table">
          <thead >
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Vaccines</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {vaccineTypes.map(v => (
            <tr key={v.vaccineTypeId}>
              <td>{v.vaccineTypeId}</td>
              <td>{v.vaccineTypeName}</td>
              <td>{v.vaccineTypeDescription}</td>
              <td>
                {expandedVaccineType === v.vaccineTypeName ? (
          <ul style={{ textAlign: "left", marginTop: "10px", paddingLeft: "20px" }}>
                    {vaccines[v.vaccineTypeName]?.map(vaccine => (
                      <li key={vaccine.vaccineId}style={{ marginBottom: "8px", display: "flex", alignItems: "center", gap: "8px" }}>
                        {vaccine.vaccineName}
                        <button style={{
                    backgroundColor: "#30c3da",
                    borderColor: "#30c3da",
                    color: "white",
                    padding: "0.25rem 0.5rem",
                    fontSize: "0.875rem",
                    borderRadius: "0.2rem",
                    display: "flex",
                    alignItems: "center",
                    cursor: "pointer"
                  }}
                          onClick={() => updateVaccine(
                            vaccine.vaccineId,
                            vaccine.vaccineName,
                            v.vaccineTypeName,
                            vaccine.description,
                            vaccine.recommendedAgeWeeks
                          )}
                        >
                  <FaEdit style={{ marginRight: "5px" }} /> Update
                        </button>
                        <button
                  style={{
                    backgroundColor: "#dc3545",
                    borderColor: "#dc3545",
                    color: "white",
                    padding: "0.25rem 0.5rem",
                    fontSize: "0.875rem",
                    borderRadius: "0.2rem",
                    display: "flex",
                    alignItems: "center",
                    cursor: "pointer"
                  }}
                          onClick={() => deleteVaccine(vaccine.vaccineId, v.vaccineTypeName)}
                        >
                  <FaTrash style={{ marginRight: "5px" }} /> Delete
                        </button>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <button  style={{
              backgroundColor: "#6c757d",
              borderColor: "#6c757d",
              color: "white",
              padding: "0.25rem 0.5rem",
              fontSize: "0.875rem",
              borderRadius: "0.2rem",
              display: "flex",
              alignItems: "center",
              cursor: "pointer"
            }}
 
                   onClick={() => showVaccines(v.vaccineTypeName)}>  <FaEye style={{ marginRight: "5px" }} /> Show Breeds
                   Show Vaccines</button>
                )}
              </td>
              <td>
               <div style={{ display: "flex", gap: "5px", flexWrap: "wrap" }}>

                <button             style={{
              backgroundColor: "#023C5A",
              borderColor: "#023C5A",
              color: "white",
              padding: "0.25rem 0.5rem",
              fontSize: "0.875rem",
              borderRadius: "0.2rem",
              display: "flex",
              alignItems: "center",
              cursor: "pointer"}}
 onClick={() => addVaccine(v.vaccineTypeId, v.vaccineTypeName)}><FaPlus style={{ marginRight: "5px" }} /> Add</button>
                <button             style={{
              backgroundColor: "#30c3da",
              borderColor: "#30c3da",
              color: "white",
              padding: "0.25rem 0.5rem",
              fontSize: "0.875rem",
              borderRadius: "0.2rem",
              display: "flex",
              alignItems: "center",
              cursor: "pointer",
              margin: "0 0.5rem"
            }}
 onClick={() => updateVaccineType(v.vaccineTypeId, v.vaccineTypeName, v.vaccineTypeDescription)}>  <FaEdit style={{ marginRight: "5px" }} />Edit</button>
                <button             style={{
              backgroundColor: "#dc3545",
              borderColor: "#dc3545",
              color: "white",
              padding: "0.25rem 0.5rem",
              fontSize: "0.875rem",
              borderRadius: "0.2rem",
              display: "flex",
              alignItems: "center",
              cursor: "pointer"
            }}
 onClick={() => deleteVaccineType(v.vaccineTypeId)}>  <FaTrash style={{ marginRight: "5px" }} />Delete</button>
            </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
 </div>
</div>
  );
}

export default Vaccines;
