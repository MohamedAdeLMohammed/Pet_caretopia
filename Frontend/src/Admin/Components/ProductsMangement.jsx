import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import ProductSearchBar from "./ProductSearchBar"; // ✅ Import the new component
import "../CSS/AdminDashboard";
import {FaPaw}from "react-icons/fa";
function ProductsMangement() {
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;

  useEffect(() => {
    const getProducts = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/products`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setProducts(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load products", "error");
      }
    };

    getProducts();
  }, []);

  return (
     <div className="products-management-container">
      <div className="products container">
        <h2 className="management-dashboard-title">Products Management</h2>  

         <div className="product-search-container">    
      <ProductSearchBar setProducts={setProducts} token={token} />
         </div>

      <Link to={'addProduct'} className="Add-btn">Add Product</Link>
      
         <div className="table-responsive">
          <table className="table products-table">
          <thead className="thead-light">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Stock</th>
            <th>Category</th>
            <th>Price</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.length > 0 ? products.map(product => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.description}</td>
              <td>{product.stockQuantity}</td>
              <td>{product.category}</td>
              <td>{product.price}</td>
              <td>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={async () => {
                    try {
                      await axios.delete(`https://localhost:8088/products/${product.id}`, {
                        headers: {
                          Authorization: `Bearer ${token}`,
                          'Content-Type': 'application/json'
                        }
                      });
                      Swal.fire("Deleted", "Product removed", "success");
                      setProducts(prev => prev.filter(p => p.id !== product.id));
                    } catch (error) {
                      Swal.fire("Error", "Unauthorized or failed", "error");
                    }
                  }}
                >
                  Remove
                </button>
                <Link to={`update/${product.id}`}>update</Link>
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

export default ProductsMangement;
