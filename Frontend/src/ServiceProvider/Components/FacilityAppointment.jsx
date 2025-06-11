import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import AppointmentSearchBar from "./AppointmentSearchBar";

function FacilityAppointment() {
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const { facilityId } = useParams();

  const AppointmentStatus = {
    PENDING: "PENDING",
    APPROVED: "ACCEPTED",
    REJECTED: "REJECTED",
    TREATED: "TREATED",
    NOT_TREATED: "NOT_TREATED"
  };

  useEffect(() => {
    const getAppointments = async () => {
      try {
        const response = await axios.get(
          `https://localhost:8088/appointment-requests/facility/${facilityId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        setAppointments(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load appointments", "error");
      }
    };

    getAppointments();
  }, [facilityId, token]);

  const markAppointmentAsTreated = async (requestId) => {
    try {
      // First, get the full appointment by requestId
      const appointmentRes = await axios.get(
        `https://localhost:8088/appointments/appointmentRequest/${requestId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        }
      );

      const appointment = appointmentRes.data;
      console.log(appointment)
      // Then, mark it as TREATED
      await axios.patch(
        `https://localhost:8088/appointments/serviceProvider/${appointment.serviceProviderId}/appointment/${appointment.appointmentId}/status`,
        null,
        {
          params: { status: AppointmentStatus.TREATED },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      Swal.fire("Treated", "Appointment marked as treated", "success");

      // Update local state
      setAppointments(prev =>
        prev.map(p =>
          p.requestId === requestId
            ? { ...p, status: AppointmentStatus.TREATED }
            : p
        )
      );
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to mark as treated", "error");
    }
  };

  return (
    <div className="container p-4">
      <h2 className="mb-4">Facility Appointments</h2>

      <AppointmentSearchBar setAppointments={setAppointments} token={token} />

      <table className="table text-center">
        <thead className="thead-light">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Reason</th>
            <th>Requested Time</th>
            <th>Status</th>
            <th colSpan={2}>Actions</th>
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
                {appointment.status === AppointmentStatus.PENDING && (
                  <>
                    <button
                      className="btn btn-success btn-sm me-2"
                      onClick={async () => {
                        try {
                          await axios.patch(
                            `https://localhost:8088/appointment-requests/serviceProvider/${appointment.serviceProviderId}/appointmentRequest/${appointment.requestId}/status`,
                            {},
                            {
                              params: { status: AppointmentStatus.APPROVED },
                              headers: {
                                Authorization: `Bearer ${token}`,
                                'Content-Type': 'application/json',
                              },
                            }
                          );
                          Swal.fire("Approved", "Appointment approved", "success");
                          setAppointments(prev =>
                            prev.map(p =>
                              p.requestId === appointment.requestId
                                ? { ...p, status: AppointmentStatus.APPROVED }
                                : p
                            )
                          );
                        } catch (error) {
                          Swal.fire("Error", "Unauthorized or failed", "error");
                        }
                      }}
                    >
                      Approve
                    </button>

                    <button
                      className="btn btn-danger btn-sm"
                      onClick={async () => {
                        try {
                          await axios.patch(
                            `https://localhost:8088/appointment-requests/facility/${facilityId}/appointmentRequest/${appointment.requestId}/status`,
                            {},
                            {
                              params: { status: AppointmentStatus.REJECTED },
                              headers: {
                                Authorization: `Bearer ${token}`,
                                'Content-Type': 'application/json',
                              },
                            }
                          );
                          Swal.fire("Rejected", "Appointment rejected", "info");
                          setAppointments(prev =>
                            prev.map(p =>
                              p.requestId === appointment.requestId
                                ? { ...p, status: AppointmentStatus.REJECTED }
                                : p
                            )
                          );
                        } catch (error) {
                          Swal.fire("Error", "Unauthorized or failed", "error");
                        }
                      }}
                    >
                      Reject
                    </button>
                  </>
                )}

                {appointment.status === AppointmentStatus.APPROVED && (
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => markAppointmentAsTreated(appointment.requestId)}
                  >
                    Treated
                  </button>
                )}
              </td>
            </tr>
          )) : (
            <tr>
              <td colSpan="9">No appointments found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default FacilityAppointment;
