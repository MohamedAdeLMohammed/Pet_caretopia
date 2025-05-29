import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import ProductSearchBar from "./ProductSearchBar"; // ✅ Import the new component

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
    <div className="container p-4">
      <h2 className="mb-4">Products</h2>
      
      {/* ✅ Add Search Component */}
      <ProductSearchBar setProducts={setProducts} token={token} />

      <Link to={'addProduct'} className="btn btn-primary mb-3">Add Product</Link>
      
      <table className="table text-center">
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
              </td>
            </tr>
          )) : (
            <tr>
              <td colSpan="7">No products found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ProductsMangement;
