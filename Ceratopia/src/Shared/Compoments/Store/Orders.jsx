import { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import Swal from 'sweetalert2';

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
      <button
        className="btn btn-primary btn-sm me-2"
        onClick={() => navigate("/dashboard/store/order-summary", { state: { order } })}
      >
        View Summary
      </button>
      {decode.role === "ADMIN" && localStatus !== "CANCELLED" && (
  <>
    {localStatus !== "DELIVERED" && nextStatusMap[localStatus] && (
      <button
        className="btn btn-warning btn-sm me-2"
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
        const response = await axios.get(`https://localhost:8088/orders/${userId}`, {
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
    <div className="container p-4">
      <h2 className="mb-4">My Orders</h2>
      <table className="text-center table table-striped adopt-reqs-table">
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
            <tr>
              <td colSpan="5">No orders found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default Orders;

