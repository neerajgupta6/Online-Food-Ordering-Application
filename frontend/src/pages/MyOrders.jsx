import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

const STATUS_LABEL = {
  PLACED: "Placed",
  PREPARING: "Preparing",
  OUT_FOR_DELIVERY: "Out for delivery",
  DELIVERED: "Delivered",
};

function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/orders/my")
      .then((res) => setOrders(res.data))
      .catch(() => setError("Could not load your orders."))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="message">Loading...</p>;
  if (error) return <p className="message error">{error}</p>;

  return (
    <div className="container">
      <h1>My Orders</h1>

      {orders.length === 0 ? (
        <>
          <p className="message">You have no orders yet.</p>
          <p className="message"><Link to="/">Order something tasty →</Link></p>
        </>
      ) : (
        orders.map((o) => (
          <div className="order-card" key={o.id}>
            <div className="order-head">
              <div>
                <strong>Order #{o.id}</strong>
                <p className="order-date">
                  {new Date(o.createdAt).toLocaleString()}
                </p>
              </div>
              <span className={`status status-${o.status}`}>
                {STATUS_LABEL[o.status] || o.status}
              </span>
            </div>

            {o.items.map((i, idx) => (
              <div className="summary-row" key={idx}>
                <span>{i.foodName} × {i.quantity}</span>
                <span>₹{Number(i.subtotal).toFixed(2)}</span>
              </div>
            ))}

            <div className="summary-row total">
              <span>Total</span>
              <span>₹{Number(o.total).toFixed(2)}</span>
            </div>
            <p className="order-address">📍 {o.address}</p>
          </div>
        ))
      )}
    </div>
  );
}

export default MyOrders;