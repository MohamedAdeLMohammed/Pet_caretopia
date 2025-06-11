import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import FacilitySearchBar from "./FacilitySearchBar";
import "../../CSS/AdminDashboard.css";
import {FaPaw, FaTrashAlt, FaEdit, FaPlusCircle} from 'react-icons/fa';

function FacilitiesMangement() {
  const [facilities, setFacilities] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;

  useEffect(() => {
    const getFacilities = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/facilities/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setFacilities(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load products", "error");
      }
    };

    getFacilities();
  }, []);

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">Facilities Management</h2>
      
      {/* ✅ Add Search Component */}
 <FacilitySearchBar setFacilities={setFacilities} token={token} />
        <div className="table-responsive">
          <table className="table products-table">
            <thead>
    <tr>
      <th>ID</th>
      <th>facilityName</th>
      <th>facilityDescription</th>
      <th>facilityAddress</th>
      <th>facilityType</th>
      <th>openingTime</th>
      <th>closingTime</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    {facilities.length > 0 ? facilities.map(facility => (
      <tr key={facility.facilityId}>
        <td>{facility.facilityId}</td>
        <td>{facility.facilityName}</td>
        <td>{facility.facilityDescription}</td>
        <td>{facility.facilityAddress}</td>
        <td>{facility.facilityType}</td>
        <td>{facility.openingTime}</td>
        <td>{facility.closingTime}</td>
        <td>
          <button
            className="btn btn-danger btn-sm"
            onClick={async () => {
              try {
                await axios.delete(`https://localhost:8088/facilities/facility/${facility.facilityId}`, {
                  headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                  }
                });
                Swal.fire("Deleted", "Provider removed", "success");
                setProviders(prev => prev.filter(p => p.serviceProviderId !== provider.serviceProviderId));
              } catch (error) {
                Swal.fire("Error", "Unauthorized or failed", "error");
              }
            }}
          >
                         <FaTrashAlt /> Remove
          </button>
        </td>
      </tr>
    )) : (
      <td colSpan="7" className="text-center py-5">
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

export default FacilitiesMangement;
