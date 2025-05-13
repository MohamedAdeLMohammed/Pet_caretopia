import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';

function Wishlist() {
  const [wishlistItems, setWishlistItems] = useState([]);
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const navigate = useNavigate();

  useEffect(() => {
    const getWishlistItems = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/wishlist/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setWishlistItems(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load wishlist items", "error");
      }
    };

    getWishlistItems();
  }, []);

  const removeItem = async (itemId) => {
    try {
      await axios.delete(`https://localhost:8088/wishlist/remove?userId=${userId}&productId=${itemId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Item Removed Successfully",
        showConfirmButton: false,
        timer: 1500
      });
      setWishlistItems(prev => prev.filter(i => i.id !== itemId));
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
  };

  const clearWishlist = async () => {
    try {
      await axios.delete(`https://localhost:8088/wishlist/clear/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Wishlist Cleared Successfully",
        showConfirmButton: false,
        timer: 1500
      });
      setWishlistItems([]);
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
  };

  return (
    <div className="cart-page">
      <h2>Your Wishlist</h2>
      <div className="cart-items">
        {wishlistItems.map((item) => (
          <div className="cart-item" key={item.id}>
            <img
              src={item.images?.[0]?.url || item.imageUrls?.[0] || "https://via.placeholder.com/150"}
              alt={item.name}
              className="product-image"
            />
            <div className="item-details">
              <h4>{item.name}</h4>
              <p>{item.description}</p>
              <button className="remove-btn" onClick={() => removeItem(item.id)}>
                Remove
              </button>
              <p className="price">${item.price.toFixed(2)}</p>
            </div>
          </div>
        ))}
      </div>

      {wishlistItems.length > 0 && (
        <div className="">
          <button className="see-more-btn" onClick={clearWishlist}>
            Clear Wishlist
          </button>
        </div>
      )}
    </div>
  );
}

export default Wishlist;
