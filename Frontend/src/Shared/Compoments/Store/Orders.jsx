import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';
import{FaPaw} from "react-icons/fa";
function Orders() {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();
  const token = sessionStorage.getItem('token');
  const decode = jwtDecode(token);
  const userId = decode.id;

  const nextStatusMap = {
    PENDING: "PROCESSING",
    PROCESSING: "SHIPPED",
    SHIPPED: "DELIVERED",
    DELIVERED: null,
  };

  const updateOrderStatusInList = (orderId, newStatus) => {
    setOrders((prevOrders) =>
      prevOrders.map((order) =>
        order.id === orderId ? { ...order, orderStatus: newStatus } : order
      )
    );
  };

const OrderActions = ({ order }) => {
  const [localStatus, setLocalStatus] = useState(order.orderStatus);

  useEffect(() => {
    setLocalStatus(order.orderStatus); // keep it in sync with updated parent state
  }, [order.orderStatus]);

  const handleStatusUpdate = async () => {
    const nextStatus = nextStatusMap[localStatus];
    if (!nextStatus) return;

    try {
      const response = await axios.put(
        `https://localhost:8088/orders/status?orderId=${order.id}&status=${nextStatus}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const updatedStatus = response.data.orderStatus || nextStatus;
      updateOrderStatusInList(order.id, updatedStatus); // update parent
      setLocalStatus(updatedStatus); // update local state for button control
      Swal.fire("Success", `Order status updated to ${updatedStatus}`, "success");
    } catch (error) {
      const backendMessage = error.response?.data?.message || error.response?.data || "Failed to update status";
      Swal.fire("Error", backendMessage, "error");
    }
  };

  const handleCancel = async () => {
    try {
      await axios.put(
        `https://localhost:8088/orders/cancel/${order.id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      updateOrderStatusInList(order.id, "CANCELLED");
      setLocalStatus("CANCELLED");
      Swal.fire("Cancelled", "Order has been cancelled.", "success");
    } catch (error) {
      Swal.fire("Error", "Failed to cancel order", "error");
    }
  };

  return (
    <>
          {decode.role === "ADMIN"?(<button
        className="btn  btn-sm me-2"
          style={{
    backgroundColor: '#023C5A',
    borderColor: '#023C5A',
    color: 'white',
    transition: 'all 0.3s ease',
    padding: '0.375rem 0.75rem',
    fontSize: '0.875rem',
    lineHeight: '1.5',
    borderRadius: '0.25rem'
  }}

        onClick={() => navigate("/dashboard/storeMangement/orders/order-summary", { state: { order } })}
      >
        View Summary
      </button>):(<button
        className="btn  btn-sm me-2"
          style={{
    backgroundColor: '#023C5A',
    borderColor: '#023C5A',
    color: 'white',
    transition: 'all 0.3s ease',
    padding: '0.375rem 0.75rem',
    fontSize: '0.875rem',
    lineHeight: '1.5',
    borderRadius: '0.25rem'
  }}

        onClick={() => navigate("/dashboard/store/order-summary", { state: { order } })}
      >
        View Summary
      </button>)}
      {decode.role === "ADMIN" && localStatus !== "CANCELLED" && (
  <>
    {localStatus !== "DELIVERED" && nextStatusMap[localStatus] && (
      <button
        className="btn  btn-sm me-2"     
            style={{
          backgroundColor: '#ff3399',
          borderColor: '#ff3399',
          color: 'white',
          transition: 'all 0.3s ease',
          padding: '0.375rem 0.75rem',
          fontSize: '0.875rem',
          lineHeight: '1.5',
          borderRadius: '0.25rem'
        }}

        onClick={handleStatusUpdate}
      >
        {nextStatusMap[localStatus]}
      </button>



    )}
{(localStatus === "PENDING" || localStatus === "PROCESSING" || localStatus === "SHIPPED") && (
  <button
    className="btn btn-danger btn-sm"
    onClick={handleCancel}
  >
    Cancel Order
  </button>
)}


  </>
)}

    </>
  );
};



  useEffect(() => {
    const getOrders = async () => {
      if(decode.role === 'ADMIN'){
              try {
        const response = await axios.get(`https://localhost:8088/orders/all`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setOrders(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load orders", "error");
      }
      }else{
              try {
        const response = await axios.get(`https://localhost:8088/orders`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        setOrders(response.data);
      } catch (error) {
        console.error(error);
        Swal.fire("Error", "Failed to load orders", "error");
      }
      }
    };

    getOrders();
  }, []);

  return (
    <div className="products-management-container">
      <div className="container">
        <h2 className="products-title">Orders</h2>

       <div className="table-responsive">
          <table className="table products-table">

        <thead>
          <tr>
            <th>Order ID</th>
            <th>Created At</th>
            <th>Total Price (EGP)</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? orders.map((order) => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>{new Date(order.createdAt).toLocaleString()}</td>
              <td>{order.totalPrice}</td>
              <td>{order.orderStatus}</td>
              <td><OrderActions order={order} /></td>
            </tr>
          )) : (
                      <td colSpan="5" className="text-center py-5">
                        <div className="text-muted">
                          <FaPaw className="display-5 mb-3" />
                          <h4>No items found</h4>
                        </div>
                      </td>
          )}
        </tbody>
      </table>
      {decode.role === "ADMIN"?(<button className="continue-btn" onClick={() => navigate("/dashboard/storeMangement")}>Back to Store Mangement</button>):(<button className="continue-btn" onClick={() => navigate("/dashboard/store")}>Back to Store</button>)}
      </div>
    </div>
    </div>
  );
}

export default Orders;

