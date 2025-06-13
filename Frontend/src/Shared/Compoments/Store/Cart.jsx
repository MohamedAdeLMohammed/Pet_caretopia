import { useState, useEffect,useContext } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import "../../CSS/Store.css";
import { StoreContext } from "../Store/StoreContext";
function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;
  const navigate = useNavigate();
  const { refreshCartCount, refreshWishCount } = useContext(StoreContext);
  useEffect(() => {
    const getCartItems = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/cart/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setCartItems(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load cart items", "error");
      }
    };

    getCartItems();
  }, []);

  const removeItem = async (id) => {
    try {
      await axios.delete(`https://localhost:8088/cart/delete/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      });

      setCartItems(prev => prev.filter(item => item.id !== id));
    } catch (error) {
      console.error(error);
      Swal.fire("Error", "Failed to remove item", "error");
    }
  };

  const updateItemQuantity = async (productId, quantity) => {
    try {
      await axios.put(
        `https://localhost:8088/cart/update?userId=${userId}&productId=${productId}&quantity=${quantity}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      setCartItems(prev =>
        prev.map(item =>
          item.product.id === productId ? { ...item, quantity } : item
        )
      );
    } catch (error) {
      Swal.fire("Error", "Failed to update item quantity", "error");
      console.error(error);
    }
  };

  const total = cartItems.reduce(
    (acc, item) => acc + item.product.price * item.quantity,
    0
  );

  return (
    <div className="cart-page">
      <h2 className="management-dashboard-title">Your Shopping Cart</h2>
      <div className="cart-items">
        {cartItems.map((item) => (
          <div className="cart-item" key={item.id}>
            <img
              src={item.product.images?.[0]?.url || "https://via.placeholder.com/150"}
              alt={item.product.name}
              className="product-image"
            />
            <div className="item-details">
              <h4>{item.product.name}</h4>
              <p>{item.product.description}</p>
              <p>Available: {item.product.quantity}</p>
              <div className="quantity-controls">
                <button
                  onClick={() =>
                    item.quantity > 1 &&
                    updateItemQuantity(item.product.id, item.quantity - 1)
                  }
                >-</button>

                <span>{item.quantity}</span>

                <button
                  disabled={item.quantity >= item.product.stockQuantity}
                  onClick={() => {
                    if (item.quantity < item.product.stockQuantity) {
                      updateItemQuantity(item.product.id, item.quantity + 1);
                    } else {
                      Swal.fire({
                        icon: "warning",
                        title: "Stock limit reached",
                        text: `Only ${item.product.quantity} in stock.`,
                        timer: 2000,
                        showConfirmButton: false,
                      });
                    }
                  }}
                >+</button>
              </div>

              <p className="price">${(item.product.price * item.quantity).toFixed(2)}</p>

              <button className="remove-btn" onClick={async () => {
                try {
                  await axios.delete(
                    `https://localhost:8088/cart/item/${item.id}`,
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
                    title: "Item Removed Successfully",
                    showConfirmButton: false,
                    timer: 1500
                  });
                  setCartItems(prev => prev.filter(i => i.id !== item.id));
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
                Remove
              </button>
            </div>
          </div>
        ))}
      </div>

      <div className="cart-total">
        <h4 style={{ color: "#023C5A" }}>Total: ${total.toFixed(2)}</h4>
      </div>

      <div>
        <button
          className="place-order"
          style={{ fontSize: "20px", marginRight: "10px" }}
          onClick={async () => {
            try {
              const response = await axios.post(
                `https://localhost:8088/checkout/place?userId=${userId}`,
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
                title: "Order Placed Successfully",
                showConfirmButton: false,
                timer: 1500
              });
              navigate("/dashboard/store/order-summary", { state: { order: response.data } });
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
        >
          Place Order
        </button>

        <button
          style={{ fontSize: "20px" }}
          className="add-to-cart"
          onClick={async () => {
            try {
              await axios.delete(
                `https://localhost:8088/cart/clear`,
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
                title: "Cart Cleared Successfully",
                showConfirmButton: false,
                timer: 1500
              });
              setCartItems([]);
              navigate("/dashboard/store")
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
        >
          Clear Cart
        </button>
      </div>
            <button className="continue-btn" onClick={() => navigate("/dashboard/store")}>Back to Store</button>
    </div>
  );
}

export default Cart;
