import { useEffect, useState } from "react";
import axios from 'axios';
import Swal from "sweetalert2";
import "../../../Pet Owner/CSS/PTODashboard.css";
import { useParams } from "react-router-dom";

function ShelterAdoptationRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const token = sessionStorage.getItem('token');
  const { shelterId } = useParams();

  useEffect(() => {
    const getAdoptionsRequest = async () => {
      try {
        setLoading(true);
        const response = await axios.get('https://localhost:8088/adoptions', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        
        // Filter requests by shelterId and add default values
        const shelterRequests = response.data
          .filter(request => request.shelterId === parseInt(shelterId))
          .map(request => ({
            ...request,
            status: request.status || 'PENDING',
            petName: request.petName || 'Unknown Pet',
            requesterUserName: request.requesterUserName || 'Unknown User',
            message: request.message || 'No message provided'
          }));
        
        setRequests(shelterRequests);
      } catch (error) {
        console.error('Error fetching data:', error);
        Swal.fire("Error", "Failed to load adoption requests", "error");
      } finally {
        setLoading(false);
      }
    };
    
    getAdoptionsRequest();
  }, [token, shelterId]);

  const handleStatusUpdate = async (id, action) => {
    try {
      await axios.put(
        `https://localhost:8088/adoptions/${id}/${action}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      
      const newStatus = action === "approve" ? "APPROVED" : "REJECTED";
      setRequests(prev => 
        prev.map(r => r.id === id ? { ...r, status: newStatus } : r)
      );
      
      Swal.fire("Success", `Request ${newStatus.toLowerCase()} successfully!`, "success");
    } catch (error) {
      Swal.fire("Error", `Failed to update request status`, "error");
      console.error(error);
    }
  };

  if (loading) {
    return <div className="loading-message">Loading adoption requests...</div>;
  }

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="management-dashboard-title">Shelter Adoption Requests</h2>
        {requests.length === 0 ? (
          <p className="no-requests-message">No adoption requests found for this shelter.</p>
        ) : (
          <div className="table-responsive">
            <table className="table products-table">
              <thead>
                <tr>
                  <th scope="col">Request ID</th>
                  <th scope="col">Pet Name</th>
                  <th scope="col">Date</th>
                  <th scope="col">Requester</th>
                  <th scope="col">Message</th>
                  <th scope="col">Status</th>
                  <th scope="col">Actions</th>
                </tr>
              </thead>
              <tbody>
                {requests.map((req) => {
                  const status = req.status?.toLowerCase() || 'pending';
                  const isPending = status === 'pending';
                  
                  return (
                    <tr key={req.id}>
                      <th scope="row">{req.id}</th>
                      <td>{req.petName}</td>
                      <td>{req.adoptionDate ? new Date(req.adoptionDate).toLocaleDateString() : 'N/A'}</td>
                      <td>{req.requesterUserName}</td>
                      <td>{req.message}</td>
                      <td>
                        <span className={`status-badge ${status}`}>
                          {status.toUpperCase()}
                        </span>
                      </td>
                      <td>
                        {isPending && (
                          <>
                            <button
                              className="btn btn-success"
                              onClick={() => handleStatusUpdate(req.id, "approve")}
                            >
                              Approve
                            </button>{" "}
                            <button
                              className="btn btn-danger"
                              onClick={() => handleStatusUpdate(req.id, "reject")}
                            >
                              Reject
                            </button>
                          </>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default ShelterAdoptationRequests;