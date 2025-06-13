import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { FaShoppingCart, FaHeart } from "react-icons/fa";
import { useContext } from "react";
import { StoreContext } from "../Store/StoreContext";

function FoodProductsDetails() {
    const [foodProducts, setFoodProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const token = sessionStorage.getItem('token');
    const context = useContext(StoreContext);
    const cartCount = context?.cartCount ?? 0;
    const wishCount = context?.wishCount ?? 0;
    const navigate = useNavigate();

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

    useEffect(() => {
        const getFoodProducts = async () => {
            try {
                setLoading(true);
                const config = token ? {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                } : {};

                const response = await axios.get(
                    `https://localhost:8088/store/products/category/FOOD`,
                    config
                );
                setFoodProducts(response.data);
            } catch (error) {
                console.error(error);
                Swal.fire("Error", "Failed to fetch food products", "error");
            } finally {
                setLoading(false);
            }
        };

        getFoodProducts();
    }, [token]);

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

            if (context?.refreshCartCount) {
                await context.refreshCartCount();
            }

            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Added to cart",
                showConfirmButton: false,
                timer: 1500
            });
        } catch (error) {
            Swal.fire({
                position: "top-end",
                icon: "error",
                title: "Failed to add to cart",
                showConfirmButton: false,
                timer: 1500
            });
            console.error(error);
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

            if (context?.refreshWishCount) {
                await context.refreshWishCount();
            }

            Swal.fire({
                position: "top-end",
                icon: "success",
                title: "Added to wishlist",
                showConfirmButton: false,
                timer: 1500
            });
        } catch (error) {
            Swal.fire({
                position: "top-end",
                icon: "error",
                title: "Failed to add to wishlist",
                showConfirmButton: false,
                timer: 1500
            });
            console.error(error);
        }
    };

    if (loading) {
        return <div className="loading-message">Loading food products...</div>;
    }

    return (
        <div className="store-page">
            <nav className="store-main-nav">
                <h2>Pet Store</h2>
                                {token ? (
  <>
    <Link to="/dashboard/store/FoodProductsDetails" className="store-link">Food</Link>
    <Link to="/dashboard/store/AccessoriesProductsDetails" className="store-link">Accessories</Link>
    <Link to="/dashboard/store/ToysProductsDetails" className="store-link">Toys</Link>
    <Link to="/dashboard/store/MedicationsProductsDetails" className="store-link">Medications</Link>
  </>
) : (
  <>
    <Link to="/home/store/FoodProductsDetails" className="store-link">Food</Link>
    <Link to="/home/store/AccessoriesProductsDetails" className="store-link">Accessories</Link>
    <Link to="/home/store/ToysProductsDetails" className="store-link">Toys</Link>
    <Link to="/home/store/MedicationsProductsDetails" className="store-link">Medications</Link>
  </>
)}
                
                {token && (
                    <>
                        <Link to="/dashboard/store/Orders" className="store-link">Orders</Link>
                        <Link to="/dashboard/store/Cart" className="store-link">
                            <FaShoppingCart /> {cartCount}
                        </Link>
                        <Link to="/dashboard/store/Wishlist" className="store-link">
                            <FaHeart /> {wishCount}
                        </Link>
                    </>
                )}
            </nav>

            <div className="all-cats-container">
                <h2 className="management-dashboard-title">Food</h2>
                <div className="product-grid">
                    {foodProducts.map((product) => (
                        <div className="product-card" key={product.id}>
                            <img 
                                src={product.images?.[0]?.url || "https://via.placeholder.com/150"} 
                                alt={product.name} 
                            />
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
                    ))}
                </div>
            </div>

{token?(<button 
                className="continue-btn" 
                onClick={() => navigate("/dashboard/store")}
            >
                Back to Store
            </button>):(            <button 
                className="continue-btn" 
                onClick={() => navigate("/home/store")}
            >
                Back to Store
            </button>)}
        </div>
    );
}

export default FoodProductsDetails;