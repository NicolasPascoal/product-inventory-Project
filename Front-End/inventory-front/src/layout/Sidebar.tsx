import { useState } from "react";
import { NavLink } from "react-router-dom";
import "../sidebar.css";

export default function Sidebar() {
  const [open, setOpen] = useState(false);

  return (
    <>
      {/* BOTÃO HAMBURGUER FORA DA SIDEBAR */}
      <button className="hamburger" onClick={() => setOpen(!open)}>
        &#9776;
      </button>

      {/* SIDEBAR */}
      <aside className={`sidebar ${open ? "open" : ""}`}>
        <h2>Nicolas Dev project</h2>
        <nav>
          <NavLink to="/" onClick={() => setOpen(false)}>Dashboard</NavLink>
          <NavLink to="/products" onClick={() => setOpen(false)}>Products</NavLink>
          <NavLink to="/raw-materials" onClick={() => setOpen(false)}>Raw-Materials</NavLink>
          <NavLink to="/links" onClick={() => setOpen(false)}>Link</NavLink>
        </nav>
      </aside>
    </>
  );
}