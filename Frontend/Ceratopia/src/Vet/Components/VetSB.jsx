import { Link } from "react-router-dom";
import "../CSS/VetDashboard.css";
function VetSB(){
    return(
    <>
        <div >
        <aside className="sidebar">
            <Link to={"/VetDashboard/ManageAccounts"} className="sidebar-btn">
            <p>Manage Accounts</p>
            </Link>
            <Link to={"/VetDashboard/VetAppointment"} className="sidebar-btn">
            <p>View appointments</p>
            </Link>

            <Link to={'/Login'} className="signout-btn">
            <p>Sign Out</p>
            </Link>
        </aside>    
        </div>


    </>
    );
}
export default VetSB;