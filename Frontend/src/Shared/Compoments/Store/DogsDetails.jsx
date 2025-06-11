import { FaShoppingCart } from "react-icons/fa";
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
      const [accessories, setAccessoriesProducts] = useState([]);
              const token = sessionStorage.getItem('token');
    console.log(token);
    const decode = jwtDecode(token);
    const userId = decode.id;
  useEffect(() => {
    const getACCESSORIESProducts = async () => {
      try {
        const token = sessionStorage.getItem('token');
        const decode = jwtDecode(token);
        const userId = decode.id;

        const response = await axios.get(
          `https://localhost:8088/store/products/category/ACCESSORIES`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        console.log(response.data);
        setAccessoriesProducts(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch cat food products", "error");
      }
    };

    getACCESSORIESProducts();
  }, []);
    return (
            <div className="store-page">
              {/* Header */}
              <header className="header">
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
              </header>        
              <div className="store-header">
                <nav className="store-main-nav">
                  <h2>Pet Store / Dogs</h2>
                  <Link to="/store/pet-parents" className="store-link">All</Link>
                  <Link to="/store/cat" className="store-link">Cat</Link>
                  <Link to="/store/dog" className="store-link">Dog</Link>
                  <Link to="/store/pharmacy" className="store-link">Pharmacy</Link>
                  <Link to="/store/travel" className="store-link">Carriers & Travel</Link>
                </nav>
              </div>
        <div className="all-cats-container">
        <h2 className="management-dashboard-title">All Pet Needs</h2>
        {/* <div className="product-grid">
          {catProducts.map((product) => (
            <div className="product-card" key={product.id}>
              <img src={product.image} alt={product.name} />
              <h4 className="ProductName">{product.name}</h4>
              <p>{product.description}</p>
              <h5 className="price">{product.price}</h5>
              <button className="add-to-cart">Add to Cart</button>

            </div>
          ))}

        </div> */}
                      <div className="product-grid">
        {accessories.map((product) => (
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
  <button className="add-to-cart" onClick={async () => {
    try {
      const response = await axios.post(
        `https://localhost:8088/wishlist/add?userId=${userId}&productId=${product.id}`,
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
    Add to WishList
  </button>
          </div>
        ))}
      </div>
      </div>

        
            </div>
          );
        }
        
 
     export default CatsDetails;
     