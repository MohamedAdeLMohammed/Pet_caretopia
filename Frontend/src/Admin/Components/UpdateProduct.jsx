import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";

function UpdateProduct() {
  const { id: productId } = useParams();
  console.log(productId);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState(0);
  const [stockQuantity, setStockQuantity] = useState(0);
  const [category, setCategory] = useState("FOOD");
  const [imageFiles, setImageFiles] = useState([]);
  const token = sessionStorage.getItem("token");

  // Fetch existing product data on mount
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(`https://localhost:8088/products/${productId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
        });
        const product = response.data;
        console.log(product)
        setName(product.name);
        setDescription(product.description);
        setPrice(product.price);
        setStockQuantity(product.stockQuantity);
        setCategory(product.category);
      } catch (error) {
        console.error("Error fetching product:", error);
        Swal.fire("Error", "Could not load product data", "error");
      }
    };

    fetchProduct();
  }, [productId]);

  const handleImageChange = (e) => {
    setImageFiles([...e.target.files]);
  };

  const updateProduct = async () => {
    if (!token) {
      Swal.fire("Unauthorized", "You must be logged in as admin", "error");
      return;
    }

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
        formData.append("images", file);
      });

      const response = await axios.put(`https://localhost:8088/products/${productId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        icon: "success",
        title: "Product updated successfully",
        text: `Product ID: ${response.data.id}`,
      });
    } catch (error) {
      console.error("Error updating product:", error);
      Swal.fire({
        icon: "error",
        title: "Failed to update product",
        text: error.response?.data?.message || "An error occurred",
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    updateProduct();
  };

  return (
    <div className="p-3 container mt-5 mb-5">
      <form onSubmit={handleFormSubmit} className="post-form">
        <div className="container row g-3">
          <div className="col-md-12">
            <label className="form-label">Product Name</label>
            <input
              type="text"
              className="form-control"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="col-md-12">
            <label className="form-label">Description</label>
            <textarea
              className="form-control"
              rows="3"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Price</label>
            <input
              type="number"
              step="0.01"
              className="form-control"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              required
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Stock Quantity</label>
            <input
              type="number"
              className="form-control"
              value={stockQuantity}
              onChange={(e) => setStockQuantity(e.target.value)}
              required
            />
          </div>
          <div className="col-md-12">
            <label className="form-label">Category</label>
            <select
              className="form-select"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              required
            >
              <option value="FOOD">FOOD</option>
              <option value="TOYS">TOYS</option>
              <option value="CLOTHING">CLOTHING</option>
              <option value="ACCESSORIES">ACCESSORIES</option>
            </select>
          </div>
          <div className="col-md-12">
            <label className="form-label">Upload New Images</label>
            <input
              type="file"
              className="form-control"
              multiple
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
          <div className="d-flex gap-2 mt-3">
            <button
              type="submit"
              className="btn"
              style={{ background: "#023C5A", border: "#21293a" }}
            >
              Update Product
            </button>
            <button
              type="reset"
              className="btn btn-danger"
              onClick={() => {
                setName("");
                setDescription("");
                setPrice(0);
                setStockQuantity(0);
                setCategory("FOOD");
                setImageFiles([]);
              }}
            >
              Reset
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default UpdateProduct;
