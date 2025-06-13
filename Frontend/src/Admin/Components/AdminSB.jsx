import { Link } from "react-router-dom";
import {
  FaStore,
  FaPaw,
  FaHome,
  FaSyringe,
  FaHandsHelping,
  FaExchangeAlt,
  FaUser,
  FaRegCommentDots
} from "react-icons/fa";

import { useNavigate } from "react-router-dom";
function AdminSB() {
  const navigate = useNavigate();
  const handleLogout = () => {
    sessionStorage.removeItem("token");
    navigate("/login");
  };
  return (
    <>
      <div className="pto-sb">
        <Link to={'/dashboard/usersMangement'} className=" db-btn">
                    <FaUser/>

            <p>User Mangement</p>
            </Link>
        <Link to={"/dashboard/storeMangement"} className="db-btn">
          <FaStore />
          <p>Manage Store</p>
        </Link>
        <Link to={"/dashboard/petMangement"} className="db-btn">
          <FaPaw />
          <p>Manage Pets</p>
        </Link>
        <Link to={"/dashboard/sheltersMangement"} className="db-btn">
          <FaHome />
          <p>Manage Shelter</p>
        </Link>
        <Link to={"/dashboard/vaccineMangement"} className="db-btn">
          <FaSyringe />
          <p>Manage Vaccines</p>
        </Link>
        <Link to={"/dashboard/serviceProvidersMangement"} className="db-btn">
          <FaHandsHelping />
          <p>Manage ServiceProviders</p>
        </Link>
        <Link to={"/dashboard/adoptionTranscations"} className="db-btn">
          <FaExchangeAlt />
          <p>Adoptions Transactions</p>
        </Link>
          <Link to={"/dashboard/breedingTranscations"} className="db-btn">
          <FaExchangeAlt />
          <p>Breeding Transactions</p>
        </Link>
            <Link to={'/dashboard/chat'} className=" db-btn">
            <FaRegCommentDots/>
            <p>Chat</p>
            </Link>
      </div>
    </>
  );
}
export default AdminSB;