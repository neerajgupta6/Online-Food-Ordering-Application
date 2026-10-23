import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";

function Cart() {
    const navigate = useNavigate();
  const { items, increase, decrease, removeItem, clearCart, totalPrice } = useCart();

  if (items.length === 0) {
    return (
      <div className="container">
        <h1>Your Cart</h1>
        <p className="message">Your cart is empty.</p>
        <p className="message"><Link to="/">← Browse the menu</Link></p>
      </div>
    );
  }

  return (
    <div className="container">
      <h1>Your Cart</h1>

      {items.map((item) => (
        <div className="cart-row" key={item.id}>
          <img
            src={item.image || "https://placehold.co/80x80?text=Food"}
            alt={item.name}
          />
          <div className="cart-info">
            <h3>{item.name}</h3>
            <p>₹{item.price}</p>
          </div>

          <div className="qty">
            <button onClick={() => decrease(item.id)}>−</button>
            <span>{item.quantity}</span>
            <button onClick={() => increase(item.id)}>+</button>
          </div>

          <div className="line-total">₹{(item.price * item.quantity).toFixed(2)}</div>
          <button className="remove" onClick={() => removeItem(item.id)}>✕</button>
        </div>
      ))}

      <div className="cart-summary">
        <h2>Total: ₹{totalPrice.toFixed(2)}</h2>
        <div>
          <button className="secondary" onClick={clearCart}>Clear cart</button>
          <button className="primary" onClick={() => navigate("/checkout")}>Checkout</button>
        </div>
      </div>
    </div>
  );
}

export default Cart;