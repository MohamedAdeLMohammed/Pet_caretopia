import { useLocation, useNavigate } from "react-router-dom";
import "../../CSS/OrderSummary.css";
import { jwtDecode } from "jwt-decode";
function OrderSummary() {
  const location = useLocation();
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const order = location.state?.order;
  console.log(order);
  const decode = jwtDecode(token);
  if (!order) {
    return <div>No order data found. Please go back and try again.</div>;
  }

  const subtotal = order.orderItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );
  const deliveryFee = 30;
  const discount = 0;
  const total = subtotal + deliveryFee - discount;

  return (
    <div className="order-summary-container">
      <h2 className="management-dashboard-title">Order Summary</h2>
      <div className="order-info">
        <p><strong>Order ID:</strong> {order.id}</p>
        <p><strong>Date:</strong> {new Date(order.createdAt).toLocaleDateString()}</p>
        <p><strong>Payment:</strong> Cash on Delivery</p>
        <p><strong>Status:</strong> {order.orderStatus}</p>
        <p><strong>Address:</strong> 5 Garden City, Cairo, Egypt</p>
      </div>

      <div className="order-items">
        {order.orderItems.map((item) => (
          <div className="order-item" key={item.id}>
            
            <div>
              <img
              src={item.product.imageUrls?.[0] || "https://via.placeholder.com/150"}
              alt={item.product.name || "Product Image"}
            />
              <h4>{item.product?.name || "Product info "}</h4>
              <p>Qty: {item.quantity}</p>
              <p>Price: {item.price} EGP</p>
              <p>Total: {item.price * item.quantity} EGP</p>
            </div>
          </div>
        ))}
      </div>

      <div className="order-total">
        <p>Subtotal: {subtotal} EGP</p>
        <p>Delivery: {deliveryFee} EGP</p>
        {discount > 0 && <p>Discount: -{discount} EGP</p>}
        <h4>Total: {total} EGP</h4>
      </div>
            {decode.role === "ADMIN"?(<button className="continue-btn" onClick={() => navigate("/dashboard/storeMangement/orders")}>Back to Orders</button>):(<button className="continue-btn" onClick={() => navigate("/dashboard/store/Orders")}>Back to Orders</button>)}
    </div>
  );
}

export default OrderSummary;
