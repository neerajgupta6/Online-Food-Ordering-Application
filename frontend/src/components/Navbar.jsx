import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

function Navbar() {
  const { totalItems } = useCart();
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo">🍕 FoodOrder</Link>

      <div className="nav-right">
        <Link to="/cart">
          🛒 Cart <span className="badge">{totalItems}</span>
        </Link>

        {user ? (
          <>
            {isAdmin && <Link to="/admin">⚙️ Admin</Link>}
            <Link to="/orders">My Orders</Link>
            <span className="hello">Hi, {user.name}</span>
            <button className="logout" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;