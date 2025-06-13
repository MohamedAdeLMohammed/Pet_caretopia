import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import "../../Admin/CSS/AdminDashboard.css";
import clinic from '../../assets/clinic.jpg'
import hotel from '../../assets/hotel.jpg'
import training from '../../assets/training.jpg'
function Facilities() {
  const [facilities, setFacilities] = useState([]);
  const [serviceProviderInfo, setServiceProviderInfo] = useState({});
  const [facilityType, setFacilityType] = useState(null);
  const token = sessionStorage.getItem("token");
  const decode = jwtDecode(token);

  // Fetch service provider info
  useEffect(() => {
    const getServiceProviderInfo = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/serviceProviders/user/${decode.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        const info = response.data;
        setServiceProviderInfo(info);

        if (info.serviceProviderType === "VET") {
          setFacilityType("VETERINARY_CLINIC");
        } else if (info.serviceProviderType === "TRAINER") {
          setFacilityType("TRAINING_CENTER");
        } else if (info.serviceProviderType === "SITTER") {
          setFacilityType("PET_HOTEL");
        }
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load service provider info", "error");
      }
    };

    getServiceProviderInfo();
    console.log(serviceProviderInfo.serviceProviderId)
  }, [decode.id, token]);

  // Fetch facilities after serviceProviderId is ready
  useEffect(() => {
    if (!serviceProviderInfo.serviceProviderId) return;

    const getFacilities = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/facilities/serviceProvider/${serviceProviderInfo.serviceProviderId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setFacilities(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load shelters", "error");
      }
    };

    getFacilities();
  }, [serviceProviderInfo.serviceProviderId, token]);

  // ... (addFacility and updateFacility methods remain unchanged)

  const addFacility = () => {
    Swal.fire({
      title: "Add Facility",
      html: `
        <input type="text" id="facilityName" class="swal2-input" placeholder="Facility Name" />
        <input type="text" id="facilityDescription" class="swal2-input" placeholder="Description" />
        <input type="text" id="facilityAddress" class="swal2-input" placeholder="Address" />
        <input type="time" id="openingTime" class="swal2-input" />
        <input type="time" id="closingTime" class="swal2-input" />
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: "Add",
      preConfirm: async () => {
        try {
          const facilityName = document.getElementById("facilityName").value.trim();
          const facilityDescription = document.getElementById("facilityDescription").value.trim();
          const facilityAddress = document.getElementById("facilityAddress").value.trim();
          const openingTimeRaw = document.getElementById("openingTime").value.trim();
          const closingTimeRaw = document.getElementById("closingTime").value.trim();

          if (!facilityName || !openingTimeRaw || !facilityAddress || !closingTimeRaw) {
            Swal.showValidationMessage("Please fill in all required fields.");
            return false;
          }

          const formatTimeTo12Hour = (timeStr) => {
            const [hour, minute] = timeStr.split(":");
            const date = new Date();
            date.setHours(hour, minute);
            return date.toLocaleTimeString("en-US", {
              hour: "2-digit",
              minute: "2-digit",
              hour12: true,
            });
          };

          const openingTime = formatTimeTo12Hour(openingTimeRaw);
          const closingTime = formatTimeTo12Hour(closingTimeRaw);

          await axios.post(
            `https://localhost:8088/facilities/add/toServiceProvider/${serviceProviderInfo.serviceProviderId}`,
            {
              facilityName,
              facilityDescription,
              facilityAddress,
              openingTime,
              closingTime,
              facilityType,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
              },
            }
          );

          return true;
        } catch (error) {
          console.error("Validation or server error:", error);
          Swal.showValidationMessage("Something went wrong. Please try again.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Facility added successfully!", "success").then(() => {
          window.location.reload();
        });
      }
    });
  };

  const updateFacility = (id, currentName, currentDescription, currentAddress, currentOpeningTime, currentClosingTime) => {
    Swal.fire({
      title: "Edit Facility",
      html: `
        <input type="text" id="facilityName" class="swal2-input" placeholder="Facility Name" value="${currentName}" />
        <input type="text" id="facilityDescription" class="swal2-input" placeholder="Description" value="${currentDescription}" />
        <input type="text" id="facilityAddress" class="swal2-input" placeholder="Address" value="${currentAddress}" />
        <input type="time" id="openingTime" class="swal2-input" value="${currentOpeningTime}" />
        <input type="time" id="closingTime" class="swal2-input" value="${currentClosingTime}" />
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: "Update",
      preConfirm: async () => {
        try {
          const facilityName = document.getElementById("facilityName").value.trim();
          const facilityDescription = document.getElementById("facilityDescription").value.trim();
          const facilityAddress = document.getElementById("facilityAddress").value.trim();
          const openingTimeRaw = document.getElementById("openingTime").value.trim();
          const closingTimeRaw = document.getElementById("closingTime").value.trim();

          if (!facilityName || !openingTimeRaw || !facilityAddress || !closingTimeRaw) {
            Swal.showValidationMessage("Please fill in all required fields.");
            return false;
          }

          const formatTimeTo12Hour = (timeStr) => {
            const [hour, minute] = timeStr.split(":");
            const date = new Date();
            date.setHours(hour, minute);
            return date.toLocaleTimeString("en-US", {
              hour: "2-digit",
              minute: "2-digit",
              hour12: true,
            });
          };

          const openingTime = formatTimeTo12Hour(openingTimeRaw);
          const closingTime = formatTimeTo12Hour(closingTimeRaw);

          await axios.put(
            `https://localhost:8088/facilities/facility/${id}`,
            {
              facilityName,
              facilityDescription,
              facilityAddress,
              openingTime,
              closingTime,
              facilityType,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
              },
            }
          );

          return true;
        } catch (error) {
          console.error("Validation or server error:", error);
          Swal.showValidationMessage("Something went wrong. Please try again.");
          return false;
        }
      },
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire("Success", "Facility updated successfully!", "success").then(() => {
          window.location.reload();
        });
      }
    });
  };

  const deleteFacility = async (id) => {
    try {
      await axios.delete(`https://localhost:8088/facilities/facility/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });

      Swal.fire("Deleted", "Facility removed", "success");

      setFacilities(prevFacilities => prevFacilities.filter(facility => facility.facilityId !== id));
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to delete facility", "error");
    }
  };

  return (
       <div className="store-management-container">
        <h2 className="management-dashboard-title">Facilities Management</h2>
      <button onClick={addFacility} className="Add-btn">Add Facility</button>

      {facilities.length === 0 ? (
        <p style={{ marginTop: "20px" }}>No Facilities found. Please add one.</p>
      ) : (
        <div className="management-grid">
          {facilities.map((facility) => (
            <div key={facility.facilityId} className="management-card">
                            {facilityType==="VETERINARY_CLINIC"&& (<img
                src={clinic}
                alt="Facility"
                style={{ width: "100%", height: "150px", objectFit: "cover", borderRadius: "8px" }}
              />)}
                {facilityType==="TRAINING_CENTER"&& (<img
                src={training}
                alt="Facility"
                style={{ width: "100%", height: "150px", objectFit: "cover", borderRadius: "8px" }}
              />)}
                              {facilityType==="PET_HOTEL"&& (<img
                src={hotel}
                alt="Facility"
                style={{ width: "100%", height: "150px", objectFit: "cover", borderRadius: "8px" }}
              />)}

              <h4 className="management-dashboard-title">{facility.facilityName}</h4>
              <Link to={`facilityAppointment/${facility.facilityId}`} className="add-to-cart">
                Manage Facility
              </Link>
              <button className="management-card-button" onClick={() => updateFacility(
                facility.facilityId,
                facility.facilityName,
                facility.facilityDescription,
                facility.facilityAddress,
                facility.openingTime,
                facility.closingTime
              )}>
                Edit
              </button>
              <button className="management-card-button" onClick={() => deleteFacility(facility.facilityId)}>Delete</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Facilities;
