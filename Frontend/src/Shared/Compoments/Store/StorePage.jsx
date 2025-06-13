import { FaShoppingCart, FaHeart } from "react-icons/fa";
import { Link } from "react-router-dom";
import StoreSlider from "./StoreSlider";
import CategoryProducts from "./CatogeryProducts";
import "../../CSS/Store.css";
import { useContext } from "react";
import { StoreContext } from "../Store/StoreContext";

function StorePage() {
  const token = sessionStorage.getItem("token");
  const context = useContext(StoreContext);
  
  // Safely extract context values (fallback to 0 if undefined)
  const cartCount = context?.cartCount ?? 0;
  const wishCount = context?.wishCount ?? 0;
  console.log(cartCount)
  return (
    <div className="store-page">
      <div className="store-header">
        <nav className="store-main-nav">
          <h2>Pet Store</h2>

          
          {/* Auth-protected links */}
          {token?(<><Link to="/dashboard/store/FoodProductsDetails" className="store-link">Food</Link>
          <Link to="/dashboard/store/AccessoriesProductsDetails" className="store-link">Accessories</Link>
          <Link to="/dashboard/store/ToysProductsDetails" className="store-link">Toys</Link>
          <Link to="/dashboard/store/MedicationsProductsDetails" className="store-link">Medications</Link>
              <Link to="/dashboard/store/Orders" className="store-link">Orders</Link>
              <Link to="/dashboard/store/Cart" className="store-link">
                <FaShoppingCart /> {cartCount}
              </Link>
              <Link to="/dashboard/store/Wishlist" className="store-link">
                <FaHeart /> {wishCount}
              </Link></>):(<><Link to="FoodProductsDetails" className="store-link">Food</Link>
          <Link to="AccessoriesProductsDetails" className="store-link">Accessories</Link>
          <Link to="ToysProductsDetails" className="store-link">Toys</Link>
          <Link to="MedicationsProductsDetails" className="store-link">Medications</Link></>)}
        </nav>
      </div>

      <StoreSlider />
      <CategoryProducts category="FOOD" title="Food" />
      <CategoryProducts category="ACCESSORIES" title="Accessories & Carriers" />
      <CategoryProducts category="TOYS" title="Toys" />
      <CategoryProducts category="MEDICATIONS" title="Medications" />
    </div>
  );
}

export default StorePage;