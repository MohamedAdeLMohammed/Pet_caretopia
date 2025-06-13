import { useEffect, useState } from "react";
import axios from 'axios';
import Swal from "sweetalert2";
import { 
  FaPaw, 
  FaCalendarAlt, 
  FaUser, 
  FaFilter, 
  FaCheck, 
  FaTimes, 
  FaClock,
  FaListUl
} from "react-icons/fa";
import "../../CSS/AdminDashboard.css";

function BreedingTranscations() {
  const [allRequests, setAllRequests] = useState([]);
  const [requests, setRequests] = useState([]);
  const [statusFilter, setStatusFilter] = useState(""); // "" = all
  const [isLoading, setIsLoading] = useState(true);
  const token = sessionStorage.getItem('token');

  const fetchRequests = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get('https://localhost:8088/breeding-requests/all', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setAllRequests(response.data);
      setRequests(response.data); // initially show all
    } catch (error) {
      console.error('Error fetching data:', error);
      Swal.fire("Error", "Failed to fetch adoption transactions.", "error");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchRequests(); // Fetch once on load
  }, []);

  const handleFilterClick = (status) => {
    setStatusFilter(status);
    if (status === "") {
      setRequests(allRequests);
    } else {
      const filtered = allRequests.filter((req) => req.status === status);
      setRequests(filtered);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "ACCEPTED":
        return <span className="badge bg-success"><FaCheck className="me-1" /> Approved</span>;
      case "REJECTED":
        return <span className="badge bg-danger"><FaTimes className="me-1" /> Rejected</span>;
      case "PENDING":
        return <span className="badge bg-warning text-dark"><FaClock className="me-1" /> Pending</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  return (
        <div className="products-management-container">
          <div className="container">
            <h2 className="products-title">Adoption Transactions</h2>
        <div className="text-muted">
          Total: {requests.length} request{requests.length !== 1 ? 's' : ''}
        </div>
      </div>

      <div className="card shadow-sm mb-4">
        <div className="card-header bg-light">
          <h5 className="mb-0">
            <FaFilter className="me-2" /> Filter Requests
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
              <FaCheck className="me-1" /> Approved
            </button>
            <button 
              className={`btn ${statusFilter === "REJECTED" ? 'btn-danger' : 'btn-outline-danger'}`} 
              onClick={() => handleFilterClick("REJECTED")}
            >
              <FaTimes className="me-1" /> Rejected
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
              <p className="mt-2">Loading adoption requests...</p>
            </div>
          ) : (
          <div className="table-responsive">
              <table className="table products-table">
                <thead>
  
                  <tr>
                    <th scope="col"><FaPaw className="me-1" /> Male Pet</th>
                    <th scope="col"><FaPaw className="me-1" /> Female Pet</th>
                    <th scope="col">Name</th>
                    <th scope="col"><FaCalendarAlt className="me-1" /> Date</th>
                    <th scope="col"><FaUser className="me-1" /> Requester</th>
                    <th scope="col"><FaUser className="me-1" /> Receiver</th>
                    <th scope="col">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {requests.length > 0 ? (
                    requests.map((req) => (
                      <tr key={req.id} className="align-middle">
                        <td>#{req.id}</td>
                        <td>
                          <strong>{req.malePet.petName}</strong>
                        </td>
                        <td>
                          <strong>{req.femalePet.petName}</strong>
                        </td>
                        <td>{new Date(req.requestDate).toLocaleDateString()}</td>
                        <td>{req.requester.user.name}</td>
                        <td>{req.receiver.user.name}</td>
                        <td>{getStatusBadge(req.status)}</td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="5" className="text-center py-5">
                        <div className="text-muted">
                          <FaPaw className="display-5 mb-3" />
                          <h4>No adoption requests found</h4>
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

export default BreedingTranscations;