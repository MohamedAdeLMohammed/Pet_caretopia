import { Link } from "react-router-dom";
import "../../CSS/AdminDashboard.css";

function PetMangement(){
    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">Pets Management</h2>


        <div className="product-grid">
            <div className="product-card">
              <img src={'../src/assets/pet management.jpg'} alt={''} />
              <h4 className="ProductName">Pets </h4>
              <Link to={'petTypesMangement'} className="add-to-cart">Manage Pet Types</Link>
            </div>
            
        </div>
      </div>
        </>
    );
}
export default PetMangement;