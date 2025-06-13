import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import { useParams } from "react-router-dom";
import "../../CSS/AdminDashboard.css";
import {FaPaw, FaTrashAlt, FaEdit, FaPlusCircle} from 'react-icons/fa';

function Shelter() {
  const [pets, setPets] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const { shelterId: shelterId } = useParams();
  useEffect(() => {
    const getPets = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/shelters/${shelterId}/pets`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setPets(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load products", "error");
      }
    };

    getPets();
  }, []);

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">Shelter Pets Management</h2>
      
      {/* ✅ Add Search Component
      <ProductSearchBar setProducts={setProducts} token={token} /> */}

      <Link to={`addPet`} className="Add-btn"> <FaPlusCircle /> Add Pet</Link>
      <Link to={`shelterAdoptionReqests`} className="Add-btn"> <FaPlusCircle /> Shelter Adoption Request</Link>
        <div className="table-responsive">
          <table className="table products-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Breed Type</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pets.length > 0 ? pets.map(pet => (
            <tr key={pet.petID}>
              <td>{pet.petID}</td>
              <td>{pet.petName}</td>
              <td>{pet.petTypeName}</td>
              <td>{pet.petBreedName}</td>
              <td>
                <div className="action-btns">
                <button
                  className=" btn-danger "
                  onClick={async () => {
                    try {
                      await axios.delete(`https://localhost:8088/pets/${pet.petID}`, {
                        headers: {
                          Authorization: `Bearer ${token}`,
                          'Content-Type': 'application/json'
                        }
                      });
                      Swal.fire("Deleted", "Product removed", "success");
                      setPets(prev => prev.filter(p => p.id !== pet.id));
                    } catch (error) {
                      Swal.fire("Error", "Unauthorized or failed", "error");
                    }
                  }}
                >
                  <FaTrashAlt /> Remove
                </button>
                <Link to={`updatePet/${pet.petID}`} className="update-link"> <FaEdit />update</Link>
             </div>
              </td>
            </tr>
          )) : (
                      <td colSpan="5" className="text-center py-5">
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

export default Shelter;
