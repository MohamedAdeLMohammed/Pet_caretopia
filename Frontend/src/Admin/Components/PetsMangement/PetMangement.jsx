import { Link } from "react-router-dom";
import "../../CSS/AdminDashboard.css";

function PetMangement(){
    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">Pets Management</h2>


        <div className="management-grid">
            <div className="management-card">
              <img src={'../src/assets/pet management.jpg'} alt={''} />
              <h4 className="management-card-title">Pets </h4>
              <Link to={'petTypesMangement'} className="management-card-button">Manage Pet Types</Link>
            </div>
            
        </div>
      </div>
        </>
    );
}
export default PetMangement;



