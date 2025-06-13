import { useState, useEffect, useContext } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import "../../CSS/Store.css";
import { StoreContext } from "../Store/StoreContext";

function Wishlist() {
  const [wishlistItems, setWishlistItems] = useState([]);
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const navigate = useNavigate();
  const { refreshCartCount, refreshWishCount } = useContext(StoreContext);

  // ✅ Define this function to check if product is already in cart
  const isProductInCart = async (productId) => {
    try {
      const response = await axios.get(`https://localhost:8088/cart/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      const cartItems = response.data;
      return cartItems.some(item => item.product.id === productId);
    } catch (error) {
      console.error("Error checking cart:", error);
      return false;
    }
  };

  const handleAddToCart = async (productId) => {
    try {
      const exists = await isProductInCart(productId);
      if (exists) {
        Swal.fire({
          position: "top-end",
          icon: "info",
          title: "Product already in cart",
          showConfirmButton: false,
          timer: 1500
        });
        return;
      }

      await axios.post(
        `https://localhost:8088/cart/add?userId=${userId}&productId=${productId}&quantity=1`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      await refreshCartCount();
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added to cart",
        showConfirmButton: false,
        timer: 1500
      });
      navigate("/dashboard/store/cart");
    } catch (error) {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Unauthorized",
        showConfirmButton: false,
        timer: 1500
      });
    }
  };

  useEffect(() => {
    const getWishlistItems = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/wishlist`, {
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
      await axios.delete(`https://localhost:8088/wishlist/remove?&productId=${itemId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      await refreshWishCount();
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
      await axios.delete(`https://localhost:8088/wishlist/clear`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      await refreshWishCount();
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
      <h2 className="management-dashboard-title">Your Wishlist</h2>
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
              {item.stockQuantity > 0 ? (
                <button
                  className="add-to-cart"
                  onClick={() => handleAddToCart(item.id)}
                >
                  Add to Cart
                </button>
              ) : (
                <p className="add-to-cart">Sold out</p>
              )}
              <p className="price">${item.price.toFixed(2)}</p>
            </div>
          </div>
        ))}
      </div>

      {wishlistItems.length > 0 && (
        <div>
          <button
            style={{ fontSize: "20px", marginTop: "10px" }}
            className="add-to-cart"
            onClick={clearWishlist}
          >
            Clear Wishlist
          </button>
        </div>
      )}
      <button className="continue-btn" onClick={() => navigate("/dashboard/store")}>Back to Store</button>
    </div>
  );
}

export default Wishlist;
