import { useEffect, useState } from "react";
import cat from '../../assets/cat 1.png'
import axios from 'axios';
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import { jwtDecode } from 'jwt-decode';
function TrainingCenterAppointments(){
    const [facilities,setFacilities] = useState([]);
    const token = sessionStorage.getItem('token');
    const decode = jwtDecode(token);
    const userId = decode.id;
useEffect(() => {
    const getFacilites = async () => {
        try {
            const response = await axios.get('https://localhost:8088/facilities/type?type=TRAINING_CENTER',{
              headers: {
              Authorization: `Bearer ${token}`,
            },
            });
            console.log(response.data);
            setFacilities(response.data)  // This logs the product details
            // You can set state here, for example:
            // setPets(response.data.vaccines.$values)
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    getFacilites();
    }, []);
    const formatDateTime = (date) => {
  const pad = (n) => String(n).padStart(2, '0');

  let hours = date.getHours();
  const minutes = pad(date.getMinutes());
  const ampm = hours >= 12 ? 'PM' : 'AM';

  hours = hours % 12;
  hours = hours ? hours : 12; // Convert 0 to 12 for 12 AM

  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(hours)}:${minutes} ${ampm}`;
};

const requestAppointment = (facilityId, serviceProviderId) => {
  Swal.fire({
    title: "Request Appointment",
    html: `
      <input type="text" id="appointment-reason" class="swal2-input" placeholder="What is Appointment Reason?" />
      <input type="text" id="appointment-time" class="swal2-input" placeholder="Requested Time (e.g., 2025-06-05 09:30 AM)" />
    `,
    showCancelButton: true,
    confirmButtonText: "Add",
    preConfirm: async () => {
      const reason = document.getElementById("appointment-reason").value.trim();
      const requestedTime = document.getElementById("appointment-time").value.trim();

      if (!reason || !requestedTime) {
        Swal.showValidationMessage("All fields are required.");
        return false;
      }

      // Validate time format using a simple regex
      const dateTimeRegex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2} (AM|PM)$/;
      if (!dateTimeRegex.test(requestedTime)) {
        Swal.showValidationMessage("Time format must be: YYYY-MM-DD hh:mm AM/PM");
        return false;
      }

      try {
        await axios.post(`https://localhost:8088/appointment-requests/add/user/${userId}`, {
          facilityId,
          serviceProviderId,
          requestReason: reason,
          requestedTime,
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        return true;
      } catch (error) {
        console.error("Error adding appointment:", error);
        Swal.showValidationMessage("Failed to request Appointment.");
        return false;
      }
    },
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire("Success", "Appointment registered successfully!", "success");
    }
  });
};





    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">Training Centers</h2>
        <div className="management-grid">
      {facilities.map((facility) => (
          <div className="management-card" key={facility.facilityId}>
            <img
              src="#"
            />
            <h4 className="management-card-title">{facility.facilityName}</h4>
            <p>{facility.facilityDescription}</p>
            <h5 className="price">${facility.facilityAddress}</h5>
            <button className="management-card-button" onClick={()=>{
                requestAppointment(facility.facilityId,facility.serviceProvider.serviceProviderId)}}>
    request for Appointment
  </button>
  
          </div>
        ))}
        </div>
      </div>
        </>
    );
}
export default TrainingCenterAppointments;