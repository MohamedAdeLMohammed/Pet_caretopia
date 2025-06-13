import { Link } from "react-router-dom";
import "../../CSS/AdminDashboard.css";

function StoreMangement(){
    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">Store Management</h2>

        <div className="management-grid">
            <div className="management-card">
              <img src={'../src/assets/pet order.jpg'} alt={''} />
              <h4 className="management-card-title">Orders</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}
            <Link to={'orders'} className="management-card-button">Mange Orders</Link>
            </div>

            <div className="management-card">
              <img src={'../src/assets/pet product.jpg'} alt={''} />
              <h4 className="management-card-title">Products</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}

              <Link to={'products'} className="management-card-button">Mange Products</Link>
            </div>
            
        </div>
      </div>
        </>
    );
}
export default StoreMangement;