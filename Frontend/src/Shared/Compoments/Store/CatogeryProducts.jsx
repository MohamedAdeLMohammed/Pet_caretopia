import { Link } from "react-router-dom";
import { useEffect, useState, useContext } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { StoreContext } from "../Store/StoreContext";
import { useNavigate } from "react-router-dom";
function CategoryProducts({ category, title }) {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const token = sessionStorage.getItem('token');
  const navigate = useNavigate("");
  // Safely destructure context with default values
  const context = useContext(StoreContext);
  const refreshCartCount = context?.refreshCartCount || (() => {});
  const refreshWishCount = context?.refreshWishCount || (() => {});

  // Get products by category
  useEffect(() => {
    const getProductsByCategory = async () => {
      try {
        setLoading(true);
        const config = token ? {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        } : {};
        
        const response = await axios.get(
          `https://localhost:8088/store/products/category/${category}`,
          config
        );
        setProducts(response.data.slice(0, 4));
        setError(null);
      } catch (err) {
        console.error("Error fetching products:", err);
        setError(`Failed to load ${title} products`);
        Swal.fire("Error", `Failed to fetch ${title} products`, "error");
      } finally {
        setLoading(false);
      }
    };

    getProductsByCategory();
  }, [category, token, title]);

  const showLoginAlert = () => {
    Swal.fire({
      title: "Login Required",
      text: "You must login to use store features",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Login",
      cancelButtonText: "Cancel"
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = "/login";
      }
    });
  };

  const handleAddToCart = async (productId) => {
  if (!token) {
            // Store the current path before redirecting to login
            sessionStorage.setItem('redirectAfterLogin', location.pathname);

            Swal.fire({
                title: "Login Required",
                text: "You must be logged in to request an appointment",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Login",
                cancelButtonText: "Cancel"
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate("/login", { 
                        state: { from: location.pathname } 
                    });
                }
            });
            return;
        }

    try {
      const decode = jwtDecode(token);
      const userId = decode.id;

      // Check if product already in cart
      const response = await axios.get(
        `https://localhost:8088/cart/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        }
      );

      const exists = response.data.some(item => item.product.id === productId);
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

      // Add to cart
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

      // Refresh cart count if context available
      if (context?.refreshCartCount) {
        await refreshCartCount();
      }

      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added to cart",
        showConfirmButton: false,
        timer: 1500
      });
    } catch (error) {
      console.error("Error adding to cart:", error);
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Failed to add to cart",
        showConfirmButton: false,
        timer: 1500
      });
    }
  };

  const handleAddToWishlist = async (productId) => {
    if (!token) {
            // Store the current path before redirecting to login
            sessionStorage.setItem('redirectAfterLogin', location.pathname);

            Swal.fire({
                title: "Login Required",
                text: "You must be logged in to request an appointment",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Login",
                cancelButtonText: "Cancel"
            }).then((result) => {
                if (result.isConfirmed) {
                    navigate("/login", { 
                        state: { from: location.pathname } 
                    });
                }
            });
            return;
        }

    try {
      const decode = jwtDecode(token);
      const userId = decode.id;

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
      
      // Refresh wishlist count if context available
      if (context?.refreshWishCount) {
        await refreshWishCount();
      }

      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Added to wishlist",
        showConfirmButton: false,
        timer: 1500
      });
    } catch (error) {
      console.error("Error adding to wishlist:", error);
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Failed to add to wishlist",
        showConfirmButton: false,
        timer: 1500
      });
    }
  };

  if (loading) {
    return <div className="loading-spinner">Loading {title} products...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="all-cats-container">
      <h2 className="management-dashboard-title">{title}</h2>
      
      {products.length === 0 ? (
        <div className="no-products">No {title} products available</div>
      ) : (
        <>
          <div className="product-grid">
            {products.map((product) => (
              <div className="product-card" key={product.id}>
                <img 
                  src={product.images?.[0]?.url || "https://via.placeholder.com/150"} 
                  alt={product.name} 
                  className="product-image"
                />
                <div className="product-details">
                  <h4 className="product-name">{product.name}</h4>
                  <p className="product-description">{product.description}</p>
                  <h5 className="product-price">${product.price.toFixed(2)}</h5>
                  <div className="product-stock">
                    {product.stockQuantity > 0 ? (
                      <span className="in-stock">In Stock</span>
                    ) : (
                      <span className="out-of-stock">Sold Out</span>
                    )}
                  </div>
                </div>

                <div className="product-actions">
                  {product.stockQuantity > 0 ? (
                    <button
                      className="add-to-cart"
                      onClick={() => handleAddToCart(product.id)}
                    >
                      Add to Cart
                    </button>
                  ) : (
                    <button className="add-to-cart disabled" disabled>
                      Sold Out
                    </button>
                  )}

                  <button
                    className="add-to-cart"
                    onClick={() => handleAddToWishlist(product.id)}
                  >
                    Add to Wishlist
                  </button>
                </div>
              </div>
            ))}
          </div>
          <Link className="see-more-btn" to={`${category}ProductsDetails`}>
            View All {title} Products →
          </Link>
        </>
      )}
    </div>
  );
}

export default CategoryProducts;