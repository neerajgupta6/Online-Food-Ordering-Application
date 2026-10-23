import { useCart } from "../context/CartContext";

function FoodCard({ food }) {
  const { addToCart } = useCart();

  const image =
    food.image ||
    "https://placehold.co/300x200?text=" + encodeURIComponent(food.name);

  return (
    <div className="card">
      <img src={image} alt={food.name} />
      <div className="card-body">
        <h3>{food.name}</h3>
        <p className="category">{food.categoryName}</p>
        <p className="desc">{food.description}</p>
        <div className="card-footer">
          <span className="price">₹{food.price}</span>
          <button onClick={() => addToCart(food)}>Add to cart</button>
        </div>
      </div>
    </div>
  );
}

export default FoodCard;