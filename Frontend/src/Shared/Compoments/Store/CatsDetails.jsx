import { FaShoppingCart } from "react-icons/fa";
import ProjectLogo from "../../../assets/Blue Retro Animals & Pets Logo.png";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
const catProducts = [
    {
      id: 1,
      name: "Cat Dry Food",
      description: "Healthy, crunchy meals for daily nutrition",
      image: "src/assets/cat dry food.avif",
      price:"$500",
    },
    {
      id: 2,
      name: "Cat litter",
      description: "Odor-absorbing litter for a clean home",
      image: "src/assets/cat litter.avif",
      price:"$500",

    },
    {
      id: 3,
      name: "Cat Beds",
      description: "Cozy spots for peaceful & comfortable cat naps",
      image: "src/assets/cat beds.avif",
      price:"$500",

    },
    {
      id: 4,
      name: "Cat Dry Food",
      description: "Healthy, crunchy meals for daily nutrition",
      image: "src/assets/cat dry food.avif",
      price:"$500",

    },
    {
      id: 5,
      name: "Cat litter",
      description: "Odor-absorbing litter for a clean home",
      image: "src/assets/cat litter.avif",
      price:"$500",

    },
    {
      id: 6,
      name: "Cat Beds",
      description: "Cozy spots for peaceful & comfortable cat naps",
      image: "src/assets/cat beds.avif",
      price:"$500",

    },
    {
      id: 7,
      name: "Cat Dry Food",
      description: "Healthy, crunchy meals for daily nutrition",
      image: "src/assets/cat dry food.avif",
      price:"$500",

    },
    {
      id: 8,
      name: "Cat litter",
      description: "Odor-absorbing litter for a clean home",
      image: "src/assets/cat litter.avif",
      price:"$500",

    },
    {
      id: 9,
      name: "Cat Beds",
      description: "Cozy spots for peaceful & comfortable cat naps",
      image: "src/assets/cat beds.avif",
      price:"$500",

    },
    {
        id: 10,
        name: "Cat Beds",
        description: "Cozy spots for peaceful & comfortable cat naps",
        image: "src/assets/cat beds.avif",
        price:"$500",

      },
  
  ];


function CatsDetails(){
    const [foodProducts, setFoodProducts] = useState([]);

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
                    <FaShoppingCart className="cart-icon" />
                  </div>
                </div>
              </header>         */}
              <div className="store-header">
                <nav className="store-main-nav">
                  <h2>Pet Store / Cats</h2>
                  <Link to="Pet-parents" className="store-link">All</Link>
                  <Link to="Cat" className="store-link">Cat</Link>
                  <Link to="Dog" className="store-link">Dog</Link>
                  <Link to="Pharmacy" className="store-link">Pharmacy</Link>
                  <Link to="Travel" className="store-link">Carriers & Travel</Link>
                  <Link to="Cart" className="store-link"><FaShoppingCart/></Link>
                </nav>
              </div>
        <div className="all-cats-container">
        <h2 className="management-dashboard-title">All Cats Needs</h2>
        <div className="product-grid">
          {foodProducts.map((product) => (
            <div className="product-card" key={product.id}>
              <img src={product.images?.[0]?.url || "https://via.placeholder.com/150"} alt={product.name} />
              <h4 className="ProductName">{product.name}</h4>
              <p>{product.description}</p>
              <h5 className="price">{product.price}</h5>
                          <button className="add-to-cart" onClick=
            {async () => {
  try {
    const response = await axios.post(
      `https://localhost:8088/cart/add?userId=${userId}&productId=${product.id}&quantity=1`,
      {}, // request body
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
}}
>Add to Cart</button>

            </div>
          ))}

        </div>
      </div>

        
            </div>
          );
        }
        
 
     export default CatsDetails;
     