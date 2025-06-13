import { useEffect, useState } from "react";
import axios from 'axios';
import Swal from "sweetalert2";
import "../../CSS/PTODashboard.css";

function AdoptationRequests() {
  const [requests, setRequests] = useState([]);
  const token = sessionStorage.getItem('token');

  useEffect(() => {
    const getAdoptionsRequest = async () => {
      try {
        const response = await axios.get('https://localhost:8088/adoptions/mine', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log(response.data);
        setRequests(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    getAdoptionsRequest();
  }, [token]);

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="management-dashboard-title">Adoption Requests</h2>
        <div className="table-responsive">
          <table className="table products-table">
            <thead>
              <tr>
                <th scope="col">Pet-Id</th>
                <th scope="col">Pet Name</th>
                <th scope="col">Date</th>
                <th scope="col">Requester</th>
                <th scope="col">Message</th>
                <th scope="col">Status</th>
                <th scope="col">Handle</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req) => (
                <tr key={req.id}>
                  <th scope="row">{req.id}</th>
                  <td>{req.petName}</td>
                  <td>{req.adoptionDate}</td>
                  <td>{req.requesterUserName}</td>
                  <td>{req.message}</td>
                  <td>{req.status}</td>
                  <td>
                    {req.status !== "APPROVED" && req.status !== "REJECTED" && (
                      <>
                        <button
                          className="btn btn-success"
                          onClick={async () => {
                            try {
                              await axios.put(
                                `https://localhost:8088/adoptions/${req.id}/approve`,
                                {},
                                {
                                  headers: {
                                    Authorization: `Bearer ${token}`,
                                    'Content-Type': 'application/json',
                                  },
                                }
                              );
                              Swal.fire("Success", "Request approved successfully!", "success");
                              setRequests((prev) =>
                                prev.map((r) =>
                                  r.id === req.id ? { ...r, status: "APPROVED" } : r
                                )
                              );
                            } catch (error) {
                              Swal.fire("Error", "Failed to approve request", "error");
                            }
                          }}
                        >
                          Approve
                        </button>{" "}
                        <button
                          className="btn btn-danger"
                          onClick={async () => {
                            try {
                              await axios.put(
                                `https://localhost:8088/adoptions/${req.id}/reject`,
                                {},
                                {
                                  headers: {
                                    Authorization: `Bearer ${token}`,
                                    'Content-Type': 'application/json',
                                  },
                                }
                              );
                              Swal.fire("Success", "Request rejected successfully!", "success");
                              setRequests((prev) =>
                                prev.map((r) =>
                                  r.id === req.id ? { ...r, status: "REJECTED" } : r
                                )
                              );
                            } catch (error) {
                              Swal.fire("Error", "Failed to reject request", "error");
                            }
                          }}
                        >
                          Reject
                        </button>
                      </>
                    )}
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

export default AdoptationRequests;
