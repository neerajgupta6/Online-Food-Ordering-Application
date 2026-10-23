import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axios";
import { useCart } from "../context/CartContext";

function Checkout() {
  const { items, totalPrice, clearCart } = useCart();
  const navigate = useNavigate();
  const [address, setAddress] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  if (items.length === 0) {
    return (
      <div className="container">
        <h1>Checkout</h1>
        <p className="message">Your cart is empty.</p>
        <p className="message"><Link to="/">← Browse the menu</Link></p>
      </div>
    );
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      await api.post("/orders", {
        address,
        items: items.map((i) => ({ foodId: i.id, quantity: i.quantity })),
      });
      clearCart();
      navigate("/orders");
    } catch (err) {
      setError(err.response?.data?.message || "Could not place the order. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Checkout</h1>

      <div className="checkout-grid">
        <form className="checkout-form" onSubmit={handleSubmit}>
          <h2>Delivery address</h2>
          {error && <p className="form-error">{error}</p>}
          <textarea
            rows="4"
            placeholder="House no, street, area, city, pincode"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            required
            minLength={10}
          />
          <button type="submit" disabled={loading}>
            {loading ? "Placing order..." : `Place order · ₹${totalPrice.toFixed(2)}`}
          </button>
        </form>

        <div className="order-summary">
          <h2>Order summary</h2>
          {items.map((i) => (
            <div className="summary-row" key={i.id}>
              <span>{i.name} × {i.quantity}</span>
              <span>₹{(i.price * i.quantity).toFixed(2)}</span>
            </div>
          ))}
          <div className="summary-row total">
            <span>Total</span>
            <span>₹{totalPrice.toFixed(2)}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Checkout;