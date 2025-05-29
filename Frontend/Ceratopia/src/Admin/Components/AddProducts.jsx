import { useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";

function AddProducts() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [stockQuantity, setStockQuantity] = useState(0);
  const [category, setCategory] = useState("FOOD");
  const [imageFiles, setImageFiles] = useState([]);
  const token = localStorage.getItem("token"); // Or however you store your token

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const addProduct = async () => {
    try {
      const product = {
        name,
        description,
        price: parseFloat(price),
        stockQuantity: parseInt(stockQuantity),
        category,
      };

      const formData = new FormData();
      formData.append("product", new Blob([JSON.stringify(product)], { type: "application/json" }));

      imageFiles.forEach((file) => {
        formData.append("images", file); // MUST use "images" (plural)
      });

      await axios.post("https://localhost:8088/products/product", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Product added successfully",
        showConfirmButton: false,
        timer: 1500,
      });
    } catch (error) {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "Failed to add product",
        text: error.response?.data?.message || "An error occurred",
        showConfirmButton: true,
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    addProduct();
  };

  return (
    <div className="p-3 container mt-5 mb-5">
      <form onSubmit={handleFormSubmit} className="post-form">
        <div className="container row g-3">
          <div className="col-md-12">
            <label className="form-label">Product Name</label>
            <input type="text" className="form-control" onChange={(e) => setName(e.target.value)} />
          </div>
          <div className="col-md-12">
            <label className="form-label">Description</label>
            <textarea className="form-control" rows="3" onChange={(e) => setDescription(e.target.value)} />
          </div>
          <div className="col-md-6">
            <label className="form-label">Price</label>
            <input type="number" className="form-control" onChange={(e) => setPrice(e.target.value)} />
          </div>
          <div className="col-md-6">
            <label className="form-label">Stock Quantity</label>
            <input type="number" className="form-control" onChange={(e) => setStockQuantity(e.target.value)} />
          </div>
          <div className="col-md-12">
            <label className="form-label">Category</label>
            <select className="form-select" onChange={(e) => setCategory(e.target.value)}>
              <option value="FOOD">FOOD</option>
              <option value="TOYS">TOYS</option>
              <option value="CLOTHING">CLOTHING</option>
              <option value="ACCESSORIES">ACCESSORIES</option>
            </select>
          </div>
          <div className="col-md-12">
            <label className="form-label">Upload Images</label>
            <input type="file" className="form-control" multiple accept="image/*" onChange={handleImageChange} />
          </div>
          <div className="d-flex gap-2 mt-3">
            <button type="submit" className="btn btn-success" style={{ background: "#21293a", border: "#21293a" }}>
              Add Product
            </button>
            <button type="reset" className="btn btn-danger">
              Reset
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default AddProducts;
