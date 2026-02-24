import { useEffect, useState } from "react";
import { api } from "../services/api";
import "../pages.css"
import type { Product } from "../types/product";
import EditProductModal from "../components/ProductModal.tsx";
import ConfirmDeleteModal from "../components/ConfirmModal.tsx";


export default function Products() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [isCreating, setIsCreating] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState<Product | null>(null);

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    try {
      const response = await api.get("/products");
      setProducts(response.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }

  function handleDeleteClick(product: Product) {
  setDeleteTarget(product);
}
async function confirmDelete() {
  if (!deleteTarget) return;

  try {
    await api.delete(`/products/${deleteTarget.id}`);
    setDeleteTarget(null);
    loadProducts();
  } catch (error) {
    console.error(error);
  }
}


  async function handleSave(product: Product) {
    try {
      if (isCreating) {
        await api.post("/products", {
          name: product.name,
          price: product.price,
        });
      } else {
        await api.put(`/products/${product.id}`, {
          name: product.name,
          price: product.price,
        });
      }

      setIsModalOpen(false);
      setSelectedProduct(null);
      setIsCreating(false);
      loadProducts();
    } catch (error) {
      console.error(error);
    }
  }


  function handleOpenEdit(product: Product) {
    setSelectedProduct(product);
    setIsModalOpen(true);
  }

 return (
  <div className="container">
    <div className="header">
      <h1>Products</h1>
      <button
        onClick={() => {
          setSelectedProduct({ id: 0, name: "", price: 0 });
          setIsCreating(true);
          setIsModalOpen(true);
        }}
      >
        Add Product
      </button>
    </div>

    {/* Tabela de produtos */}
    {loading ? (
      <p>Loading products...</p>
    ) : (
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>R$ {p.price.toFixed(2)}</td>
              <td>
                <button
                  style={{ background: "#2563eb", marginRight: "8px" }}
                  onClick={() => handleOpenEdit(p)}
                >
                  Edit
                </button>
                <button
                  style={{ background: "#ef4444" }}
                  onClick={() => handleDeleteClick(p)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    )}

    {/* Modal */}
    {isModalOpen && selectedProduct && (
      <EditProductModal
        product={selectedProduct}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
        isCreating={isCreating}
      />
    )}
    {deleteTarget && (
  <ConfirmDeleteModal
    title="Delete product"
    description={`Are you sure you want to delete "${deleteTarget.name}"?`}
    onConfirm={confirmDelete}
    onCancel={() => setDeleteTarget(null)}
  />
)}
  </div>
);

}
