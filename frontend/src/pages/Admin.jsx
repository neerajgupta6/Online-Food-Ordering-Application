import { useState } from "react";
import AdminFoods from "../components/admin/AdminFoods";
import AdminCategories from "../components/admin/AdminCategories";
import AdminOrders from "../components/admin/AdminOrders";

const TABS = ["Orders", "Foods", "Categories"];

function Admin() {
  const [tab, setTab] = useState("Orders");

  return (
    <div className="container">
      <h1>Admin Panel</h1>

      <div className="categories">
        {TABS.map((t) => (
          <button
            key={t}
            className={t === tab ? "chip active" : "chip"}
            onClick={() => setTab(t)}
          >
            {t}
          </button>
        ))}
      </div>

      {tab === "Orders" && <AdminOrders />}
      {tab === "Foods" && <AdminFoods />}
      {tab === "Categories" && <AdminCategories />}
    </div>
  );
}

export default Admin;