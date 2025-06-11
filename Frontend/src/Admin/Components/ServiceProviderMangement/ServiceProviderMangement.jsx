import { Link } from "react-router-dom";
import "../../CSS/AdminDashboard.css";

function ServiceProviderMangement(){
    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">ServiceProvider Mangement</h2>

        <div className="management-grid">
            <div className="management-card">
              <img src={'../src/assets/provider.jpg'} alt={''} />
              <h4 className="management-card-title">Service Providers</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}
            <Link to={'providersMangement'} className="management-card-button">Mange Providers</Link>
            </div>
            <div className="management-card">
              <img src={'../src/assets/shelter.jpg'} alt={''} />
              <h4 className="management-card-title">Facilities</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}

              <Link to={'facilitiesMangement'} className="management-card-button">Mange Facilities</Link>
            </div>
            
        </div>
      </div>
        </>
    );
}
export default ServiceProviderMangement;