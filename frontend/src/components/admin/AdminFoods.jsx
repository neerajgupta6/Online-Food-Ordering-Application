import { useEffect, useState } from "react";
import api from "../../api/axios";

const EMPTY = {
  name: "",
  price: "",
  categoryId: "",
  image: "",
  description: "",
  available: true,
};

function AdminFoods() {
  const [foods, setFoods] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(EMPTY);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  const load = async () => {
    try {
      const [f, c] = await Promise.all([
        api.get("/admin/foods"),
        api.get("/admin/categories"),
      ]);
      setFoods(f.data);
      setCategories(c.data);
    } catch {
      setError("Could not load data.");
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const resetForm = () => {
    setForm(EMPTY);
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSaving(true);

    const payload = {
      ...form,
      price: Number(form.price),
      categoryId: Number(form.categoryId),
    };

    try {
      if (editingId) {
        await api.put(`/admin/foods/${editingId}`, payload);
      } else {
        await api.post("/admin/foods", payload);
      }
      resetForm();
      load();
    } catch (err) {
      setError(err.response?.data?.message || "Could not save the food.");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (food) => {
    setEditingId(food.id);
    setForm({
      name: food.name,
      price: food.price,
      categoryId: food.categoryId ?? "",
      image: food.image || "",
      description: food.description || "",
      available: food.available,
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (food) => {
    if (!window.confirm(`Delete "${food.name}"?`)) return;
    setError("");
    try {
      await api.delete(`/admin/foods/${food.id}`);
      if (editingId === food.id) resetForm();
      load();
    } catch (err) {
      setError(err.response?.data?.message || "Could not delete the food.");
    }
  };

  return (
    <div>
      {error && <p className="form-error">{error}</p>}

      <form className="admin-form" onSubmit={handleSubmit}>
        <h3>{editingId ? `Edit food #${editingId}` : "Add new food"}</h3>

        <div className="form-grid">
          <input name="name" placeholder="Food name" value={form.name}
                 onChange={handleChange} required />
          <input name="price" type="number" step="0.01" min="0.01" placeholder="Price (₹)"
                 value={form.price} onChange={handleChange} required />

          <select name="categoryId" value={form.categoryId}
                  onChange={handleChange} required>
            <option value="">Select category</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>

          <input name="image" placeholder="Image URL (optional)"
                 value={form.image} onChange={handleChange} />
        </div>

        <textarea name="description" rows="2" placeholder="Description (optional)"
                  value={form.description} onChange={handleChange} />

        <label className="check">
          <input type="checkbox" name="available"
                 checked={form.available} onChange={handleChange} />
          Available to customers
        </label>

        <div className="form-buttons">
          <button type="submit" disabled={saving}>
            {saving ? "Saving..." : editingId ? "Update food" : "Add food"}
          </button>
          {editingId && (
            <button type="button" className="secondary" onClick={resetForm}>
              Cancel
            </button>
          )}
        </div>
      </form>

      <table className="admin-table">
        <thead>
          <tr>
            <th>ID</th><th>Name</th><th>Category</th>
            <th>Price</th><th>Status</th><th></th>
          </tr>
        </thead>
        <tbody>
          {foods.map((f) => (
            <tr key={f.id}>
              <td>{f.id}</td>
              <td>{f.name}</td>
              <td>{f.categoryName}</td>
              <td>₹{f.price}</td>
              <td>
                <span className={f.available ? "pill ok" : "pill off"}>
                  {f.available ? "Available" : "Hidden"}
                </span>
              </td>
              <td className="actions">
                <button onClick={() => startEdit(f)}>Edit</button>
                <button className="danger" onClick={() => handleDelete(f)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminFoods;