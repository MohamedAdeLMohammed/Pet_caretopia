import { FaShoppingCart , FaHeart } from "react-icons/fa";
import { Link } from "react-router-dom";
import StoreSlider from "./StoreSlider";
import Cats from "./Cats";
import Dogs from "./Dogs";
import Travel from "./Travel";
import '../../CSS/Store.css'
import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';

function StorePage() {
    const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const [cartItems,setCartItems] = useState('');
  const [wishItems,setWishItems] = useState('');
  useEffect(() => {
    const getWishCount = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/wishlist/count`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setWishItems(response.data);
        console.log(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load orders", "error");
      }
    };

    getWishCount();
  }, []);
    useEffect(() => {
    const getCartCount = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/cart/count`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setCartItems(response.data);
        console.log(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load orders", "error");
      }
    };

    getCartCount();
  }, []);
  return (
    <div className="store-page">
      <div className="store-header">
        <nav className="store-main-nav">
          <h2 >Pet Store</h2>
          <Link to="FoodProductsDetails" className="store-link">Food</Link>
          <Link to="DogsDetails" className="store-link">Accessories & Carriers</Link>
          <Link to="Travel" className="store-link">Toys</Link>
          <Link to="Orders" className="store-link">Orders</Link>
          <Link to="Cart" className="store-link"><FaShoppingCart/>{cartItems}</Link>
          <Link to="Wishlist" className="store-link"><FaHeart />{wishItems}</Link>
        </nav>
      </div>
      <StoreSlider />
      <Cats />
      <Dogs />
      <Travel />

    </div>
  );
}

export default StorePage;