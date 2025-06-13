import { Link } from "react-router-dom";
import "../CSS/TrainerDashboard.css";
function TrainerSB(){
    return(
    <>
        <div >
        <aside className="sidebar">
            <Link to={"/TrainerDashboard/ManageService"} className="sidebar-btn">
            <p>Manage Services</p>
            </Link>

            <Link to={"/TrainerDashboard/TrainerAppointment"} className="sidebar-btn">
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
export default TrainerSB;