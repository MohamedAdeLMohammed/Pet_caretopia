import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";
import {FaPaw, FaTrashAlt} from 'react-icons/fa';

function MyAppointments() {
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const { facilityId } = useParams();

  useEffect(() => {
    const getAppointments = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/appointment-requests/user/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setAppointments(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load appointments", "error");
      }
    };

    getAppointments();
  }, [token, userId]);

  const handleCancel = async (requestId) => {
    try {
      await axios.put(
        `https://localhost:8088/appointment-requests/user/${userId}/appointmentRequest/${requestId}`,
        { status: "CANCELED" },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      
      // Update the local state to reflect the canceled appointment
      setAppointments(prev => 
        prev.map(appointment => 
          appointment.requestId === requestId 
            ? { ...appointment, status: "CANCELED" } 
            : appointment
        )
      );
      
      Swal.fire("Canceled", "Appointment has been canceled", "success");
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to cancel appointment", "error");
    }
  };

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">My Appointments</h2>
      
        <div className="table-responsive">
          <table className="table products-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Reason</th>
                <th>Time</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {appointments.length > 0 ? appointments.map(appointment => (
                <tr key={appointment.requestId}>
                  <td>{appointment.requestId}</td>
                  <td>{appointment.user.name}</td>
                  <td>{appointment.user.userPhoneNumber}</td>
                  <td>{appointment.user.userEmail}</td>
                  <td>{appointment.requestReason}</td>
                  <td>{appointment.requestedTime}</td>
                  <td>{appointment.status}</td>
                  <td>
                    {appointment.status === "PENDING" && (
                      <button
                        className="btn btn-danger btn-sm me-2"
                        onClick={() => handleCancel(appointment.requestId)}
                      >
                        <FaTrashAlt /> Cancel
                      </button>
                    )}
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="8" className="text-center py-5">
                    <div className="text-muted">
                      <FaPaw className="display-5 mb-3" />
                      <h4>No appointments found</h4>
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default MyAppointments;