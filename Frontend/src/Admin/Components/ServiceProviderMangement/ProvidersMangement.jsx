import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import ProductSearchBar from "../StoreMangement/ProductSearchBar"; // ✅ Import the new component
import "../../CSS/AdminDashboard.css";
import {FaPaw, FaTrashAlt} from 'react-icons/fa';

function ProvidersMangement() {
  const [providers, setProviders] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;

  useEffect(() => {
    const getProviders = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/serviceProviders/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setProviders(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load products", "error");
      }
    };

    getProviders();
  }, []);

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">Service Providers Management</h2>
      
      {/* ✅ Add Search Component */}
      {/* <ProductSearchBar setProducts={setProducts} token={token} />

      <Link to={'addProduct'} className="btn btn-primary mb-3">Add Product</Link> */}
        <div className="table-responsive">
          <table className="table products-table">
            <thead>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Phone Number</th>
      <th>Address</th>
      <th>Gender</th>
      <th>Age</th>
      <th>Salary</th>
      <th>Type</th>
      <th>Experience</th>
      <th>Facilities</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    {providers.length > 0 ? providers.map(provider => (
      <tr key={provider.serviceProviderId}>
        <td>{provider.serviceProviderId}</td>
        <td>{provider.name}</td>
        <td>{provider.userEmail}</td>
        <td>{provider.userPhoneNumber}</td>
        <td>{provider.userAddress}</td>
        <td>{provider.userGender}</td>
        <td>{provider.age}</td>
        <td>{provider.serviceProviderSalary}</td>
        <td>{provider.serviceProviderType}</td>
        <td>{provider.serviceProviderExperience}</td>

        <td>
          {provider.facilities && provider.facilities.length > 0 ? (
            <ul className="list-unstyled mb-0">
              {provider.facilities.map(facility => (
                <li key={facility.facilityId}>
                  <strong>{facility.facilityName}</strong><br />
                </li>
              ))}
            </ul>
          ) : (
            <em>No facilities</em>
          )}
        </td>

        <td>
        <div className="action-btns">
                      <button
                        className="btn-danger"
            onClick={async () => {
              try {
                await axios.delete(`https://localhost:8088/serviceProviders/user/${provider.serviceProviderId}`, {
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
           <FaTrashAlt />  Remove
          </button>
          </div>
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

export default ProvidersMangement;
