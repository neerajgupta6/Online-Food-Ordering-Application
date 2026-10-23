import { useEffect, useState } from "react";
import api from "../../api/axios";

const NEXT = {
  PLACED: "PREPARING",
  PREPARING: "OUT_FOR_DELIVERY",
  OUT_FOR_DELIVERY: "DELIVERED",
};

const LABEL = {
  PLACED: "Placed",
  PREPARING: "Preparing",
  OUT_FOR_DELIVERY: "Out for delivery",
  DELIVERED: "Delivered",
};

function AdminOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/admin/orders")
      .then((res) => setOrders(res.data))
      .catch(() => setError("Could not load orders."))
      .finally(() => setLoading(false));
  }, []);

  const moveForward = async (order) => {
    setError("");
    try {
      const res = await api.put(`/admin/orders/${order.id}/status`, {
        status: NEXT[order.status],
      });
      setOrders((prev) => prev.map((o) => (o.id === order.id ? res.data : o)));
    } catch (err) {
      setError(err.response?.data?.message || "Could not update the status.");
    }
  };

  if (loading) return <p className="message">Loading...</p>;

  return (
    <div>
      {error && <p className="form-error">{error}</p>}
      {orders.length === 0 && <p className="message">No orders yet.</p>}

      {orders.map((o) => (
        <div className="order-card" key={o.id}>
          <div className="order-head">
            <div>
              <strong>Order #{o.id}</strong>
              <p className="order-date">{new Date(o.createdAt).toLocaleString()}</p>
              <p className="order-date">👤 {o.customerName} ({o.customerEmail})</p>
            </div>
            <span className={`status status-${o.status}`}>{LABEL[o.status]}</span>
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

          {NEXT[o.status] ? (
            <button className="advance" onClick={() => moveForward(o)}>
              Mark as {LABEL[NEXT[o.status]]} →
            </button>
          ) : (
            <p className="done">✔ Order completed</p>
          )}
        </div>
      ))}
    </div>
  );
}

export default AdminOrders;