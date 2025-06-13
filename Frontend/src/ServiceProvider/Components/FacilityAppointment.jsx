import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { 
  FaCalendarAlt, 
  FaUser, 
  FaFilter, 
  FaCheck, 
  FaTimes, 
  FaClock,
  FaListUl,
  FaThumbsUp,
  FaThumbsDown,
  FaCheckCircle,
  FaUserClock
} from "react-icons/fa";
import "../../Admin/CSS/AdminDashboard.css";
function FacilityAppointment() {
  const [allAppointments, setAllAppointments] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [treatedAppointments, setTreatedAppointments] = useState([]);
  const [facility, setFacility] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState("");
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  
  let decode;
  try {
    decode = jwtDecode(token);
  } catch (error) {
    console.error("Invalid token:", error);
    sessionStorage.removeItem('token');
    navigate('/login');
    return null;
  }

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
    const loadData = async () => {
      setIsLoading(true);
      try {
        // Fetch appointments in parallel
        const [appointmentsRes, facilityRes] = await Promise.all([
          axios.get(`https://localhost:8088/appointment-requests/facility/${facilityId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }),
          axios.get(`https://localhost:8088/facilities/facilityWithSP/${facilityId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          })
        ]);

        setAllAppointments(appointmentsRes.data);
        setAppointments(appointmentsRes.data);
        setFacility(facilityRes.data);

        if (facilityRes.data?.serviceProvider?.serviceProviderId) {
          const treatedRes = await axios.get(
            `https://localhost:8088/appointments/serviceProvider/${facilityRes.data.serviceProvider.serviceProviderId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          setTreatedAppointments(treatedRes.data);
        }
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load data", "error");
      } finally {
        setIsLoading(false);
      }
    };

    loadData();
  }, [facilityId, token, navigate]);

  const markAppointmentAsTreated = async (requestId) => {
    try {
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
      setAllAppointments(prev =>
        prev.map(p =>
          p.requestId === requestId
            ? { ...p, status: AppointmentStatus.TREATED }
            : p
        )
      );
      setAppointments(prev =>
        prev.map(p =>
          p.requestId === requestId
            ? { ...p, status: AppointmentStatus.TREATED }
            : p
        )
      );
      setTreatedAppointments(prev => [...prev, appointment]);
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to mark as treated", "error");
    }
  };

  const handleFilterClick = (status) => {
    setStatusFilter(status);
    if (status === "") {
      setAppointments(allAppointments);
    } else {
      const filtered = allAppointments.filter((appt) => {
        const isTreated = treatedAppointments.some(t => t.requestId === appt.requestId);
        const currentStatus = isTreated ? AppointmentStatus.TREATED : appt.status;
        return currentStatus === status;
      });
      setAppointments(filtered);
    }
  };

  const getStatusBadge = (appointment) => {
    const isTreated = treatedAppointments.some(t => t.requestId === appointment.requestId);
    const currentStatus = isTreated ? AppointmentStatus.TREATED : appointment.status;

    switch (currentStatus) {
      case AppointmentStatus.APPROVED:
        return <span className="badge bg-success"><FaCheck className="me-1" /> Approved</span>;
      case AppointmentStatus.REJECTED:
        return <span className="badge bg-danger"><FaTimes className="me-1" /> Rejected</span>;
      case AppointmentStatus.PENDING:
        return <span className="badge bg-warning text-dark"><FaClock className="me-1" /> Pending</span>;
      case AppointmentStatus.TREATED:
        return <span className="badge bg-info"><FaCheckCircle className="me-1" /> Treated</span>;
      default:
        return <span className="badge bg-secondary">{currentStatus}</span>;
    }
  };

  const handleApprove = async (appointment) => {
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
      
      // Update both allAppointments and filtered appointments
      const updatedAppointments = allAppointments.map(p =>
        p.requestId === appointment.requestId
          ? { ...p, status: AppointmentStatus.APPROVED }
          : p
      );
      
      setAllAppointments(updatedAppointments);
      setAppointments(updatedAppointments);
    } catch (error) {
      Swal.fire("Error", "Unauthorized or failed", "error");
    }
  };

  const handleReject = async (appointment) => {
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
      
      // Update both allAppointments and filtered appointments
      const updatedAppointments = allAppointments.map(p =>
        p.requestId === appointment.requestId
          ? { ...p, status: AppointmentStatus.REJECTED }
          : p
      );
      
      setAllAppointments(updatedAppointments);
      setAppointments(updatedAppointments);
    } catch (error) {
      Swal.fire("Error", "Unauthorized or failed", "error");
    }
  };

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">Facility Appointments</h2>
        <div className="text-muted">
          {facility.facilityName && (
            <span>Facility: {facility.facilityName} • </span>
          )}
          Total: {appointments.length} appointment{appointments.length !== 1 ? 's' : ''}
        </div>
      </div>

      <div className="card shadow-sm mb-4">
        <div className="card-header bg-light">
          <h5 className="mb-0">
            <FaFilter className="me-2" /> Filter Appointments
          </h5>
        </div>
        <div className="card-body">
          <div className="d-flex flex-wrap gap-2">
            <button 
              className={`btn ${statusFilter === "" ? 'btn-dark' : 'btn-outline-dark'}`} 
              onClick={() => handleFilterClick("")}
            >
              <FaListUl className="me-1" /> All
            </button>
            <button 
              className={`btn ${statusFilter === "PENDING" ? 'btn-primary' : 'btn-outline-primary'}`} 
              onClick={() => handleFilterClick("PENDING")}
            >
              <FaClock className="me-1" /> Pending
            </button>
            <button 
              className={`btn ${statusFilter === "ACCEPTED" ? 'btn-success' : 'btn-outline-success'}`} 
              onClick={() => handleFilterClick("ACCEPTED")}
            >
              <FaThumbsUp className="me-1" /> Approved
            </button>
            <button 
              className={`btn ${statusFilter === "REJECTED" ? 'btn-danger' : 'btn-outline-danger'}`} 
              onClick={() => handleFilterClick("REJECTED")}
            >
              <FaThumbsDown className="me-1" /> Rejected
            </button>
            <button 
              className={`btn ${statusFilter === "TREATED" ? 'btn-info' : 'btn-outline-info'}`} 
              onClick={() => handleFilterClick("TREATED")}
            >
              <FaCheckCircle className="me-1" /> Treated
            </button>
          </div>
        </div>
      </div>

      <div className="card shadow-sm">
        <div className="card-body p-0">
          {isLoading ? (
            <div className="text-center p-5">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <p className="mt-2">Loading appointments...</p>
            </div>
          ) : (
            <div className="table-responsive">
              <table className="table products-table">
                <thead>
                  <tr>
                    <th scope="col"><FaUser className="me-1" /> Patient</th>
                    <th scope="col">Contact</th>
                    <th scope="col"><FaCalendarAlt className="me-1" /> Requested Time</th>
                    <th scope="col">Reason</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments.length > 0 ? (
                    appointments.map(appointment => (
                      <tr key={appointment.requestId} className="align-middle">
                        <td>
                          <strong>{appointment.user.name}</strong>
                          <div className="text-muted small">{appointment.user.userEmail}</div>
                        </td>
                        <td>{appointment.user.userPhoneNumber}</td>
                        <td>{new Date(appointment.requestedTime).toLocaleString()}</td>
                        <td>{appointment.requestReason}</td>
                        <td>{getStatusBadge(appointment)}</td>
                        <td>
                          {appointment.status === AppointmentStatus.PENDING && !treatedAppointments.some(t => t.requestId === appointment.requestId) && (
                            <div className="d-flex gap-2">
                              <button
                                className="btn btn-success btn-sm"
                                onClick={() => handleApprove(appointment)}
                              >
                                <FaCheck /> Approve
                              </button>
                              <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleReject(appointment)}
                              >
                                <FaTimes /> Reject
                              </button>
                            </div>
                          )}
                          {appointment.status === AppointmentStatus.APPROVED && !treatedAppointments.some(t => t.requestId === appointment.requestId) && (
                            <button
                              className="btn btn-info btn-sm"
                              onClick={() => markAppointmentAsTreated(appointment.requestId)}
                            >
                              <FaCheckCircle /> Mark Treated
                            </button>
                          )}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="7" className="text-center py-5">
                        <div className="text-muted">
                          <FaUserClock className="display-5 mb-3" />
                          <h4>No appointments found</h4>
                          {statusFilter && (
                            <button 
                              className="btn btn-link" 
                              onClick={() => handleFilterClick("")}
                            >
                              Show all appointments
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default FacilityAppointment;