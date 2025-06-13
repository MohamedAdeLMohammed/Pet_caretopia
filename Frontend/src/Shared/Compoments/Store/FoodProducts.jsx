import { Link } from "react-router-dom";
import { useEffect, useState, useContext } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { StoreContext } from "../Store/StoreContext";

function FoodProducts() {
  const [foodProducts, setFoodProducts] = useState([]);
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const { refreshCartCount, refreshWishCount } = useContext(StoreContext);

  useEffect(() => {
    const getCatsProducts = async () => {
      try {
        const response = await axios.get(
          `https://localhost:8088/store/products/category/FOOD`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            },
          }
        );
        setFoodProducts(response.data.slice(0, 4));
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to fetch food products", "error");
      }
    };

    getCatsProducts();
  }, []);

  const isProductInCart = async (productId) => {
    try {
      const response = await axios.get(`https://localhost:8088/cart/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      });

      const cartItems = response.data;
      return cartItems.some(item => item.product.id === productId); // Adjust depending on your cart response shape
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
        title: "Added to cart successfully",
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
  };

  const handleAddToWishlist = async (productId) => {
    try {
      await axios.post(
        `https://localhost:8088/wishlist/add?userId=${userId}&productId=${productId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      await refreshWishCount();
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added to wishlist successfully",
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
  };

  return (
    <div className="all-cats-container">
      <h2 className="management-dashboard-title">Food</h2>
      <div className="product-grid">
        {foodProducts.map((product) => (
          <div className="product-card" key={product.id}>
            <img src={product.images?.[0]?.url || "https://via.placeholder.com/150"} alt={product.name} />
            <h4 className="ProductName">{product.name}</h4>
            <p>{product.description}</p>
            <h5 className="price">${product.price.toFixed(2)}</h5>

            {product.stockQuantity > 0 ? (
              <button
                className="add-to-cart"
                onClick={() => handleAddToCart(product.id)}
              >
                Add to Cart
              </button>
            ) : (
              <p className="add-to-cart">Sold out</p>
            )}

            <button
              className="add-to-cart"
              onClick={() => handleAddToWishlist(product.id)}
            >
              Add to Wishlist
            </button>
          </div>
        ))}
      </div>
      <Link className="see-more-btn" to={'/dashboard/store/FoodProductsDetails'}>
        <p>See More</p>
      </Link>
    </div>
  );
}

export default FoodProducts;
