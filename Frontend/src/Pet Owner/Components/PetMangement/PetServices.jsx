import { Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
function PetServices(){
    const token = sessionStorage.getItem("token");
  console.log(token);
    return(
        <>
        <div className="store-management-container">
        <h2 className="management-dashboard-title">Services</h2>
        {token ? (<Link to={'myAppointments'} className="management-card-button">My Appointments</Link>):(null)}
        <div className="management-grid">
            <div className="management-card">
              <img src={'../src/assets/adoption.png'} alt={''} />
              <h4 className="management-card-title">Training Center</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}
            <Link to={'trainingCenter'} className="management-card-button">View</Link>
            </div>
            <div className="management-card">
              <img src={'../src/assets/shelter1.jpg'} alt={''} />
              <h4 className="management-card-title">Pet Hotel</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}

              <Link to={'hotels'} className="management-card-button">View</Link>
            </div>
            <div className="management-card">
              <img src={'../src/assets/clinic.jpg'} alt={''} />
              <h4 className="management-card-title">Clinics</h4>
              {/* <p>{product.description}</p>
              <h5 className="price">{product.price}</h5> */}
              <Link to={'clinics'} className="management-card-button">View</Link>
            </div>
        </div>
      </div>
        </>
    );
}
export default PetServices;