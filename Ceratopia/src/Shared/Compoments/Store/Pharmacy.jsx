import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
const catProducts = [
    {
      id: 1,
      name: "Nexgard",
      description: "Nexgard Chewable for Large Dogs (25-50KG) 1 Tablet",
      image: "src/assets/ph7.avif",
      price:"$500.00", 
    },
    {
      id: 2,
      name: "Pets Republic",
      description: "Pets Republic Flea & Ticks Spray (125ml)",
      image: "src/assets/ph6.avif",
      price:"$400.00", 

    },
    {
      id: 3,
      name: "Poro",
      description: "Poro Pet Flea and Tick Solution for Cats 1 Pipette Sale price50.00 EGP",
      image: "src/assets/ph3.avif",
      price:"$300.00", 

    },
    {
    id: 4,
    name: "Eva pharma",
    description: "Primigo Senior Geriatric Care Plus 60 ml For Dogs With Beef flavor",
    image: "src/assets/ph1.avif",
    price:"$200.00", 

  },
{
  id: 5,
  name: "Bolovia",
  description: "Candioli Bolovia Hairball Removal Paste For Cats 50G",
  image: "src/assets/ph2.avif",
  price:"$100.00", 

},

  ];
function Pharmacy(){
    const token = sessionStorage.getItem('token');
    console.log(token);
    const decode = jwtDecode(token);
    const userId = decode.id;

    const [medcineProducts,SetMedcineProducts] = useState([]);
useEffect(() => {
  const getPharmacyProducts = async () => {
    try {
      const response = await axios.get(
        `https://localhost:8088/store/products/category/MEDICATIONS`,
        {
          headers: {
              'Content-Type': 'application/json'
          },
        }
      );
      console.log(response.data);
      SetMedcineProducts(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  getPharmacyProducts();
}, []);

    return(
        <div className="all-cats-container">
        <h2>Pharmacy</h2>
        <div className="product-grid">
      {medcineProducts.map((product) => (
          <div className="product-card" key={product.id}>
            <img
              src={product.imageUrls?.[0] || 'https://via.placeholder.com/150'}
              alt={product.name}
              className="product-image"
            />
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

    );
}
export default Pharmacy;