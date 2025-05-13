import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
const catProducts = [
    {
      id: 1,
      name: "Naomi",
      description: "Naomi Pet Crate",
      image: "src/assets/travel1.avif",
      price:"$1,000",
    },
    {
      id: 2,
      name: "Naomi Bag",
      description: "Naomi Pet Carrier Bag",
      image: "src/assets/travel2.avif",
      price:"$2,500",

    },
    {
      id: 3,
      name: "Back Bag ",
      description: "Back Bag For Cat.",
      image: "src/assets/travel3.avif",
      price:"$850"

    },
    {
    id: 4,
    name: "IATA",
    description: "Skudo Size (6) - IATA Carrier Box",
    image: "src/assets/travel4.avif",
    price:"$7,500"
  },
{
  id: 5,
  name: "Cat Bag",
  description: "Pratiko Plastic Crate for Pets",
  image: "src/assets/travel5.avif",
  price:"$650",
},

  ];
function Travel(){
        const [toys, setToysProducts] = useState([]);
                const token = sessionStorage.getItem('token');
    console.log(token);
    const decode = jwtDecode(token);
    const userId = decode.id;
  useEffect(() => {
    const getToysProducts = async () => {
      try {
        const token = sessionStorage.getItem('token');
        const decode = jwtDecode(token);
        const userId = decode.id;

        const response = await axios.get(
          `https://localhost:8088/store/products/category/TOYS`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        console.log(response.data);
        setToysProducts(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch cat food products", "error");
      }
    };

    getToysProducts();
  }, []);
    return(
        <div className="all-cats-container">
        <h2>Carriers & Travel</h2>
        <div className="product-grid">
          {toys.map((product) => (
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
      </div>

    );
}
export default Travel;