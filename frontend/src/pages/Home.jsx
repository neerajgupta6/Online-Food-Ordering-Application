import { useEffect, useState } from "react";
import api from "../api/axios";
import FoodCard from "../components/FoodCard";

function Home() {
  const [foods, setFoods] = useState([]);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("All");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .get("/foods")
      .then((res) => setFoods(res.data))
      .catch(() => setError("Could not load foods. Is the backend running?"))
      .finally(() => setLoading(false));
  }, []);

  const categories = ["All", ...new Set(foods.map((f) => f.categoryName).filter(Boolean))];

  const visibleFoods = foods.filter((f) => {
    const matchCategory = category === "All" || f.categoryName === category;
    const matchSearch = f.name.toLowerCase().includes(search.toLowerCase());
    return matchCategory && matchSearch;
  });

  if (loading) return <p className="message">Loading...</p>;
  if (error) return <p className="message error">{error}</p>;

  return (
    <div className="container">
      <h1>🍕 Online Food Order</h1>

      <input
        className="search"
        type="text"
        placeholder="Search food..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <div className="categories">
        {categories.map((c) => (
          <button
            key={c}
            className={c === category ? "chip active" : "chip"}
            onClick={() => setCategory(c)}
          >
            {c}
          </button>
        ))}
      </div>

      {visibleFoods.length === 0 ? (
        <p className="message">No food found.</p>
      ) : (
        <div className="grid">
          {visibleFoods.map((food) => (
            <FoodCard key={food.id} food={food} />
          ))}
        </div>
      )}
    </div>
  );
}

export default Home;