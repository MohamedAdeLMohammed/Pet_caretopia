import { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import "../../CSS/AdminDashboard.css";
import {FaPaw, FaTrashAlt, FaEdit, FaPlusCircle} from 'react-icons/fa';

function UsersMangement() {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const currentUserId = decode.id;

  useEffect(() => {
    const fetchUsersWithDetails = async () => {
      try {
        const summaryRes = await axios.get(`https://localhost:8088/users/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });

        const summaries = summaryRes.data;

        const detailedUsers = await Promise.all(
          summaries.map(async (summary) => {
            try {
              const detailsRes = await axios.get(`https://localhost:8088/users/user/${summary.userID}`, {
                headers: {
                  Authorization: `Bearer ${token}`,
                  'Content-Type': 'application/json'
                }
              });

              return {
                userID: summary.userID,
                name: summary.name,
                username: summary.username,
                profileImageUrl: summary.profileImageUrl,
                ...detailsRes.data
              };
            } catch (error) {
              console.error(`Error fetching details for user ${summary.userID}`, error);
              return summary;
            }
          })
        );

        setUsers(detailedUsers);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load users", "error");
      }
    };

    fetchUsersWithDetails();
  }, [token]);

  const handleDelete = async (userId) => {
    try {
      await axios.delete(`https://localhost:8088/users/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      Swal.fire("Deleted", "User removed", "success");
      setUsers(prev => prev.filter(u => u.userID !== userId));
    } catch (error) {
      Swal.fire("Error", "Unauthorized or failed", "error");
    }
  };
const handleActivate = async (userId) => {
  try {
    await axios.patch(`https://localhost:8088/users/user/${userId}`, { userStatus: "ACTIVE" }, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    Swal.fire("Updated", "User set to ACTIVE", "success");
    setUsers(prev => prev.map(u => u.userID === userId ? { ...u, userStatus: "ACTIVE" } : u));
  } catch (error) {
    Swal.fire("Error", "Failed to activate user", "error");
  }
};


const handleInActive = async (userId) => {
  try {
    await axios.patch(`https://localhost:8088/users/user/${userId}`, { userStatus: "INACTIVE" }, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    Swal.fire("Updated", "User set to INACTIVE", "success");
    setUsers(prev => prev.map(u => u.userID === userId ? { ...u, userStatus: "INACTIVE" } : u));
  } catch (error) {
    Swal.fire("Error", "Failed to update status", "error");
  }
};


const handleBanned = async (userId) => {
  try {
    await axios.patch(`https://localhost:8088/users/user/${userId}`, { userStatus: "BANNED" }, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    Swal.fire("Updated", "User BANNED", "success");
    setUsers(prev => prev.map(u => u.userID === userId ? { ...u, userStatus: "BANNED" } : u));
  } catch (error) {
    Swal.fire("Error", "Failed to ban user", "error");
  }
};


  return (
     <div className="products-management-container">
      <div className="products container">
        <h2 className="management-dashboard-title">Users Management</h2>  

         <div className="table-responsive">
          <table className="table products-table">
          <thead className="thead-light">
          <tr>
            <th>ID</th>
            <th>Profile</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Gender</th>
            <th>Age</th>
            <th>Status</th>
            <th>Address</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? users.map(user => (
            <tr key={user.userID}>
              <td>{user.userID}</td>
              <td>
                <img 
                  src={user.userImageProfile || user.profileImageUrl || "/default-profile.png"} 
                  alt={user.name} 
                  style={{ width: "50px", height: "50px", borderRadius: "50%" }} 
                />
              </td>
              <td>{user.name}</td>
              <td>{user.username}</td>
              <td>{user.userEmail}</td>
              <td>{user.userPhoneNumber}</td>
              <td>{user.userGender}</td>
              <td>{user.userAge}</td>
              <td>{user.userStatus}</td>
              <td>{user.userAddress}</td>
              <td>
                
                <br />
                {user.userStatus === "ACTIVE" ? (
  <>
    <button
      className="btn btn-warning btn-sm mb-1"
      onClick={() => handleInActive(user.userID)}
    >
      Set Inactive
    </button>
    <br />
    <button
      className="btn btn-dark btn-sm mb-1"
      onClick={() => handleBanned(user.userID)}
    >
      Ban User
    </button>
    <br />
  </>
) : (
  <>
    <button
      className="btn btn-success btn-sm mb-1"
      onClick={() => handleActivate(user.userID)}
    >
      Set Active
    </button>
    <br />
  </>
)}
              </td>
            </tr>
          )) : (
                      <td colSpan="10" className="text-center py-5">
                        <div className="text-muted">
                          <FaPaw className="display-5 mb-3" />
                          <h4>No items found</h4>
                        </div>
                      </td>
          )}
        </tbody>
      </table>
    </div>
    </div>
    </div>
  );
}

export default UsersMangement;
