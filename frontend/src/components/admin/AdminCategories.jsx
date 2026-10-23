import { useEffect, useState } from "react";
import api from "../../api/axios";

function AdminCategories() {
  const [categories, setCategories] = useState([]);
  const [name, setName] = useState("");
  const [error, setError] = useState("");

  const load = async () => {
    try {
      const res = await api.get("/admin/categories");
      setCategories(res.data);
    } catch {
      setError("Could not load categories.");
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await api.post("/admin/categories", { name });
      setName("");
      load();
    } catch (err) {
      setError(err.response?.data?.message || "Could not add category.");
    }
  };

  const handleDelete = async (c) => {
    if (!window.confirm(`Delete category "${c.name}"?`)) return;
    setError("");
    try {
      await api.delete(`/admin/categories/${c.id}`);
      load();
    } catch (err) {
      setError(err.response?.data?.message || "Could not delete category.");
    }
  };

  return (
    <div>
      {error && <p className="form-error">{error}</p>}

      <form className="admin-inline-form" onSubmit={handleAdd}>
        <input
          type="text"
          placeholder="New category name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <button type="submit">Add category</button>
      </form>

      <table className="admin-table">
        <thead>
          <tr><th>ID</th><th>Name</th><th></th></tr>
        </thead>
        <tbody>
          {categories.map((c) => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.name}</td>
              <td className="actions">
                <button className="danger" onClick={() => handleDelete(c)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminCategories;