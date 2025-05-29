import { FaShoppingCart , FaHeart } from "react-icons/fa";
import ProjectLogo from "/Ceratopia/Ceratopia/src/assets/Blue Retro Animals & Pets Logo.png";
import { Link } from "react-router-dom";
import StoreSlider from "./StoreSlider";
import Cats from "./Cats";
import Dogs from "./Dogs";
import Pharmacy from "./Pharmacy";
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
        const response = await axios.get(`https://localhost:8088/wishlist/count/${userId}`, {
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
        const response = await axios.get(`https://localhost:8088/cart/count/${userId}`, {
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
      {/* Header */}
      {/* <header className="header">
        <div className="main-header d-flex justify-content-around w-100">
          <div className="d-flex align-items-center">
            <img
              src={ProjectLogo}
              className="admindash-header-logo rounded-circle"
              alt="logo"
            />
            <h3 className="p-2">Caretopia</h3>
          </div>
          <div className="d-flex header-btns align-items-center gap-3">
            <Link to={"/Login"} className="button-header sign-up">
              Login
            </Link>
            <Link to={"/Signup"} className="button-header sign-up">
              Signup
            </Link>
            <Link to="/Cart" className="cart-icon">
  <FaShoppingCart />
</Link>
            
          </div>
        </div>
      </header> */}

      {/* Nav */}
      <div className="store-header">
        <nav className="store-main-nav">
          <h2>Pet Store</h2>
          <Link to="CatsDetails" className="store-link">Cat</Link>
          <Link to="DogsDetails" className="store-link">Dog</Link>
          <Link to="Pharmacy" className="store-link">Pharmacy</Link>
          <Link to="Travel" className="store-link">Carriers & Travel</Link>
          <Link to="Orders" className="store-link">Orders</Link>
          <Link to="Cart" className="store-link"><FaShoppingCart/>{cartItems}</Link>
          <Link to="Wishlist" className="store-link"><FaHeart />{wishItems}</Link>
        </nav>
      </div>
      <StoreSlider />
      <Cats />
      <Dogs />
      <Pharmacy />
      <Travel />

    </div>
  );
}

export default StorePage;
