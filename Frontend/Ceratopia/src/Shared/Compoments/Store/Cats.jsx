import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';

function Cats() {
  const [foodProducts, setFoodProducts] = useState([]);
          const token = sessionStorage.getItem('token');
    console.log(token);
    const decode = jwtDecode(token);
    const userId = decode.id;
  useEffect(() => {
    const getCatsProducts = async () => {
      try {
        const token = sessionStorage.getItem('token');
        const decode = jwtDecode(token);
        const userId = decode.id;

        const response = await axios.get(
          `https://localhost:8088/store/products/category/FOOD`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        console.log(response.data);
        setFoodProducts(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch cat food products", "error");
      }
    };

    getCatsProducts();
  }, []);

  return (
    <div className="all-cats-container">
      <h2>Food</h2>
      <div className="product-grid">
        {foodProducts.map((product) => (
          <div className="product-card" key={product.id}>
            <img src={product.images?.[0]?.url || "https://via.placeholder.com/150"} alt={product.name} />
            <h4 className="ProductName">{product.name}</h4>
            <p>{product.description}</p>
                        <h5 className="price">${product.price.toFixed(2)}</h5>
            {product.stockQuantity > 0 ? (
  <button className="add-to-cart" onClick={async () => {
    try {
      const response = await axios.post(
        `https://localhost:8088/cart/add?userId=${userId}&productId=${product.id}&quantity=1`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added Successfully",
        showConfirmButton: false,
        timer: 1500
      });
    } catch (error) {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Unauthorized: Please log in again",
        showConfirmButton: false,
        timer: 1500
      });
      console.error(error);
    }
  }}>
    Add to Cart
  </button>
) : (
  <p className="add-to-cart">Sold out</p>
)}
          </div>
        ))}
      </div>
      <Link className="see-more-btn" to={'/CatsDetails'}>
        <p>See More</p>
      </Link>
    </div>
  );
}

export default Cats;
