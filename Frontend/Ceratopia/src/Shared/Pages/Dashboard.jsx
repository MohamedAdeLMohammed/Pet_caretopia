import PTOwnerSB from "../../Pet Owner/Components/PTOwnerSB";
import Header from "../Compoments/Header";
import Footer from "../Compoments/Footer";
import '/Ceratopia/Ceratopia/src/Pet Owner/CSS/PTODashboard.css'
import PetsMangement from "../../Pet Owner/Components/Pets Mangements";
import PetOwnerRoutes from "../../Guard/PetOwnerRoutes";
import { Outlet, Routes ,Route ,Navigate , useNavigate } from "react-router-dom";
import PetRecoginition from "../Compoments/Pet Recoginition";
import AdoptationRequests from "../../Pet Owner/Components/AdoptationRequests";
import AdoptationOffers from "../Compoments/AdoptationOffers";
import Community from "../Compoments/Community";
import VetSB from "../../Vet/Components/VetSB"
import { jwtDecode } from 'jwt-decode';
import ServiceProviderRoutes from "../../Guard/ServiceProviderRoutes";
import ManageAccounts from "../../Vet/Components/ManageAccounts";
import VetAppointment from "../../Vet/Components/VetAppointment";
import ViewVetAppointment from "../../Vet/Components/ViewVetAppointment";
import StorePage from "../Compoments/Store/StorePage"
import StoreSlider from "../Compoments/Store/StoreSlider"
import Cart from "../Compoments/Store/Cart"
import Cats from "../Compoments/Store/Cats"
import CatsDetails from "../Compoments/Store/CatsDetails"
import Dogs from "../Compoments/Store/Dogs"
import DogsDetails from "../Compoments/Store/DogsDetails"
import Pharmacy from "../Compoments/Store/Pharmacy"
import Travel from "../Compoments/Store/Travel"
import OrderSummary from "../Compoments/Store/OrderSummary";
import Orders from "../Compoments/Store/Orders";
import StoreMangement from "../../Admin/Components/StoreMangement";
import AdminSB from "../../Admin/Components/AdminSB";
import ProductsMangement from "../../Admin/Components/ProductsMangement";
import AddProducts from "../../Admin/Components/AddProducts";
import Wishlist from "../Compoments/Store/Wishlist";
function Dashboard(){
  const token = sessionStorage.getItem("token");
  console.log(token);
        const decode = jwtDecode(token);
        console.log(decode);
        console.log(decode.role)
        console.log(decode.nameId)
        const role = decode.role;
        const isAdmin = role && (role == "ADMIN");
        const isUSER = role && (role == "USER");
        const isSERVICE_PROVIDER = role && (role =="SERVICE_PROVIDER");
        const isPET_OWNER = role && (role == "PET_OWNER");
        console.log(isAdmin)
        console.log(isUSER)
        console.log(isSERVICE_PROVIDER)
        console.log(isPET_OWNER)
        console.log(decode.role)
    return(
        <>
        <Header/>
        <div className="main-db">
          {isPET_OWNER?<PTOwnerSB/> : null}
          {isSERVICE_PROVIDER?<VetSB/> : null}
          {isAdmin?<AdminSB/> : null}
        <div className="main-content">
        <Routes>
          {isPET_OWNER?(<Route element={<PetOwnerRoutes />} >
          <Route path='pets' element={<PetsMangement />}></Route>
          <Route path='petrecoginition' element={<PetRecoginition />}></Route>
          <Route path='adoptationRequests' element={<AdoptationRequests />}></Route>
          <Route path='adoptationOffers' element={<AdoptationOffers />}></Route>
          <Route path='community' element={<Community />}></Route>
          <Route path='store' element={<StorePage/>}></Route>
          <Route path='store/CatsDetails' element={<CatsDetails/>}></Route>
          <Route path='store/DogsDetails' element={<DogsDetails/>}></Route>
          <Route path='store/Pharmacy' element={<Pharmacy/>}></Route>
          <Route path='store/Travel'element={<Travel/>}></Route>
          <Route path='store/Cart'element={<Cart/>}></Route>
          <Route path='store/order-summary'element={<OrderSummary/>}></Route>
          <Route path='store/Orders'element={<Orders/>}></Route>
          <Route path='store/Wishlist'element={<Wishlist/>}></Route>
        </Route>):null}
          {isSERVICE_PROVIDER?(<Route element={<ServiceProviderRoutes />} >
          <Route path='VetDashboard/ManageAccounts' element={<ManageAccounts />}></Route>
          <Route path='VetDashboard/VetAppointment' element={<VetAppointment />}></Route>
          {/* <Route path='/adoptationRequests' element={<ViewVetAppointment />}></Route> */}
          <Route path='community' element={<Community />}></Route>
        </Route>):null}
          {isAdmin?(<Route element={<ServiceProviderRoutes />} >
          <Route path='storeMangement' element={<StoreMangement/>}></Route>
          <Route path='storeMangement/products' element={<ProductsMangement/>}></Route>
          <Route path='storeMangement/orders' element={<Orders/>}></Route>
          <Route path='storeMangement/products/addProduct' element={<AddProducts/>}></Route>
          {/* <Route path='VetDashboard/VetAppointment' element={<VetAppointment />}></Route> */}
          {/* <Route path='/adoptationRequests' element={<ViewVetAppointment />}></Route> */}
          {/* <Route path='community' element={<Community />}></Route> */}
        </Route>):null}
        </Routes>
        </div>
        </div>
        <Footer/>
        </>
    );
}
export default Dashboard;