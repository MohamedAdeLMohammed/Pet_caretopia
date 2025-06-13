import { useEffect } from "react"; // Make sure to import useEffect
import Swal from "sweetalert2";
import PTOwnerSB from "../../Pet Owner/Components/PTOwnerSB";
import Header from "../Compoments/Home/Header";
import Footer from "../Compoments/Home/Footer";
import '../../Pet Owner/CSS/PTODashboard.css'
import PetsMangement from "../../Pet Owner/Components/PetMangement/Pets Mangements";
import PetOwnerRoutes from "../../Guard/PetOwnerRoutes";
import { Outlet, Routes ,Route ,Navigate , useNavigate } from "react-router-dom";
import PetRecoginition from "../../Pet Owner/Components/PetMangement/Pet Recoginition";
import AdoptationRequests from "../../Pet Owner/Components/PetMangement/AdoptationRequests";
import AdoptationOffers from "../../Pet Owner/Components/PetMangement/AdoptationOffers";
import Community from "../Compoments/SocialMangement/Community";
import ServiceProviderSB from "../../ServiceProvider/Components/ServiceProviderSB"
import { jwtDecode } from 'jwt-decode';
import ServiceProviderRoutes from "../../Guard/ServiceProviderRoutes";
import StorePage from "../Compoments/Store/StorePage"
import Pharmacy from "../Compoments/Store/Pharmacy"
import StoreSlider from "../Compoments/Store/StoreSlider"
import Cart from "../Compoments/Store/Cart"
import Cats from "../Compoments/Store/FoodProducts"
import FoodProductsDetails from "../Compoments/Store/FoodProductsDetails"
import Dogs from "../Compoments/Store/Accessories"
import AccessoriesDetails from "../Compoments/Store/AccessoriesDetails"
import Travel from "../Compoments/Store/Toys"
import OrderSummary from "../Compoments/Store/OrderSummary";
import Orders from "../Compoments/Store/Orders";
import StoreMangement from "../../Admin/Components/StoreMangement/StoreMangement";
import AdminSB from "../../Admin/Components/AdminSB";
import ProductsMangement from "../../Admin/Components/StoreMangement/ProductsMangement";
import AddProducts from "../../Admin/Components/StoreMangement/AddProducts";
import Wishlist from "../Compoments/Store/Wishlist";
import UpdateProduct from "../../Admin/Components/StoreMangement/UpdateProduct";
import MangePetTypes from "../../Admin/Components/PetsMangement/MangePetTypes";
import PetMangement from "../../Admin/Components/PetsMangement/PetMangement";
import ShelterMangement from "../../Admin/Components/ShelterMangement/ShelterMangement";
import Shelter from "../../Admin/Components/ShelterMangement/Shelter";
import AddPet from "../../Pet Owner/Components/PetMangement/AddPet";
import Vaccines from "../../Admin/Components/VaccinesMangement/Vaccines";
import UpdatePet from "../../Pet Owner/Components/PetMangement/UpdatePet";
import AdoptationTranscations from "../../Admin/Components/PetsMangement/AdoptionsTranscations";
import PetProfile from "../../Pet Owner/Components/PetMangement/PetProfile";
import axios from "axios";
import Facilities from "../../ServiceProvider/Components/Facilities";
import ProvidersMangement from "../../Admin/Components/ServiceProviderMangement/ProvidersMangement";
import ServiceProviderMangement from "../../Admin/Components/ServiceProviderMangement/ServiceProviderMangement";
import FacilitiesMangement from "../../Admin/Components/ServiceProviderMangement/FacilitiesMangement";
import FacilityAppointment from "../../ServiceProvider/Components/FacilityAppointment";
import MyAppointments from "../../Pet Owner/Components/Appointments/MyAppointments";
import Chat from "../Compoments/Chat";
import PetServices from "../../Pet Owner/Components/PetMangement/PetServices";
import HotelAppointments from "../../Pet Owner/Components/Appointments/HotelAppointments";
import ClinicAppointments from "../../Pet Owner/Components/Appointments/ClinicAppointments";
import TrainingCenterAppointments from "../../Pet Owner/Components/Appointments/TrainingCenterAppointments";
import UserProfile from "../Compoments/Auth/UserProfile";
import UsersMangement from "../../Admin/Components/UserMangement/UsersMangement";
import BreedingOffers from "../../Pet Owner/Components/PetMangement/BreedingOffers";
import BreedingRequests from "../../Pet Owner/Components/PetMangement/BreedingRequest";
import BreedingTranscations from "../../Admin/Components/PetsMangement/BreedingTranscations";
import { StoreProvider } from "../Compoments/Store/StoreContext";
import ShelterAdoptationRequests from "../../Admin/Components/ShelterMangement/ShelterAdoptionsRequests";
function Dashboard(){
  const token = sessionStorage.getItem("token");
  console.log(token);
        const decode = jwtDecode(token);
        console.log(decode);
        console.log(decode.role)
        console.log(decode.id)
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
          const navigate = useNavigate();
 useEffect(() => {
    if (isSERVICE_PROVIDER) {
      const fetchServiceProvider = async () => {
        try {
          const response = await axios.get(
            `https://localhost:8088/serviceProviders/user/${decode.id}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          console.log(response.data);

          if (response.data.serviceProviderType === "OTHER") {
            Swal.fire({
              title: "Choose Your Role",
              icon: "info",
              html: `
                <p>You haven't selected your service provider role yet.</p>
                <button id="choose-role-btn" class="swal2-confirm swal2-styled" style="margin-top: 10px;">Choose Role</button>
              `,
              showConfirmButton: false,
              showCloseButton: true,
              allowOutsideClick: false,
              didOpen: () => {
                const btn = Swal.getPopup().querySelector('#choose-role-btn');
                btn.addEventListener('click', () => {
                  navigate('/dashboard/userProfile');
                  Swal.close();
                });
              }
            });
          }

        } catch (error) {
          console.error("Error fetching Service Provider:", error);
        }
      };

      fetchServiceProvider();
    }
  }, [isSERVICE_PROVIDER, decode.id, token, navigate]);
    return(
        <>
        <Header/>
        <div className="main-db">
          {isPET_OWNER || isUSER?<PTOwnerSB/> : null}
          {isSERVICE_PROVIDER?<ServiceProviderSB/> : null}
          {isAdmin?<AdminSB/> : null}
        <div className="main-content">
          <StoreProvider>
                    <Routes>
              <Route 
              path="/" 
              element={
                isPET_OWNER || isUSER ? <Navigate to="/dashboard/pets" replace /> :
                isSERVICE_PROVIDER ? <Navigate to="/dashboard/facilities" replace /> :
                isAdmin ? <Navigate to="/dashboard/usersMangement" replace /> :
                <Navigate to="/" replace />
              } 
            />
          {isPET_OWNER || isUSER?(<Route element={<PetOwnerRoutes />} >
          <Route path='pets' element={<PetsMangement />}></Route>
          <Route path='userProfile' element={<UserProfile />}></Route>
          <Route path="chat" element={<Chat />} />
          <Route path="petServices" element={<PetServices />} />
          <Route path="breedingOffers" element={<BreedingOffers />} />
          <Route path="breedingRequests" element={<BreedingRequests />} />
          <Route path='petServices/hotels' element={<HotelAppointments />}></Route>
          <Route path='petServices/clinics' element={<ClinicAppointments/>}></Route>
          <Route path='petServices/trainingCenter' element={<TrainingCenterAppointments/>}></Route>
          <Route path='petServices/myAppointments' element={<MyAppointments />}></Route>
          <Route path='pets/addPet' element={<AddPet />}></Route>
          <Route path='pets/updatePet/:petID' element={<UpdatePet />}></Route>
          <Route path='pets/petProfile/:petID' element={<PetProfile />}></Route>
          <Route path='petrecoginition' element={<PetRecoginition />}></Route>
          <Route path='adoptationRequests' element={<AdoptationRequests />}></Route>
          <Route path='adoptationOffers' element={<AdoptationOffers />}></Route>
          <Route path='community' element={<Community />}></Route>

            <Route path='store' element={<StorePage/>}></Route>
            <Route path='store/FoodProductsDetails' element={<FoodProductsDetails/>}></Route>
            <Route path='store/MEDICATIONSProductsDetails' element={<Pharmacy/>}></Route>
          <Route path='store/ACCESSORIESProductsDetails' element={<AccessoriesDetails/>}></Route>
          <Route path='store/TOYSProductsDetails'element={<Travel/>}></Route>
          <Route path='store/Cart'element={<Cart/>}></Route>
          <Route path='store/order-summary'element={<OrderSummary/>}></Route>
          <Route path='store/Orders'element={<Orders/>}></Route>
          <Route path='store/Wishlist'element={<Wishlist/>}></Route>
        </Route>):null}
          {isSERVICE_PROVIDER?(<Route element={<ServiceProviderRoutes />} >
          <Route path='userProfile' element={<UserProfile />}></Route>
          <Route path='facilities' element={<Facilities />}></Route>
                      <Route path="chat" element={<Chat />} />
          <Route path='facilities/facilityAppointment/:facilityId' element={<FacilityAppointment />}></Route>
          {/* <Route path='/adoptationRequests' element={<ViewVetAppointment />}></Route> */}
          <Route path='community' element={<Community />}></Route>
        </Route>):null}
          {isAdmin?(<Route element={<ServiceProviderRoutes />} >
          <Route path='userProfile' element={<UserProfile />}></Route>
          <Route path='usersMangement' element={<UsersMangement />}></Route>
          <Route path='storeMangement' element={<StoreMangement/>}></Route>
          <Route path='storeMangement/orders/order-summary' element={<OrderSummary/>}></Route>
          <Route path='storeMangement/products' element={<ProductsMangement/>}></Route>
          <Route path='storeMangement/orders' element={<Orders/>}></Route>
          <Route path='storeMangement/products/addProduct' element={<AddProducts/>}></Route>
          <Route path='storeMangement/products/update/:id' element={<UpdateProduct/>}></Route>
          <Route path='petMangement' element={<PetMangement/>}></Route>
          <Route path='petMangement/petTypesMangement' element={<MangePetTypes/>}></Route>
          <Route path='sheltersMangement' element={<ShelterMangement/>}></Route>
          <Route path='sheltersMangement/shelter/:shelterId' element={<Shelter/>}></Route>
          <Route path='sheltersMangement/shelter/:shelterId/shelterAdoptionReqests' element={<ShelterAdoptationRequests/>}></Route>
          <Route path='sheltersMangement/shelter/:shelterId/addPet' element={<AddPet/>}></Route>
          <Route path='sheltersMangement/shelter/:shelterId/updatePet/:petID' element={<UpdatePet/>}></Route>
          <Route path='vaccineMangement' element={<Vaccines/>}></Route>
          <Route path='adoptionTranscations' element={<AdoptationTranscations/>}></Route>
          <Route path='breedingTranscations' element={<BreedingTranscations/>}></Route>
          <Route path='serviceProvidersMangement' element={<ServiceProviderMangement/>}></Route>
          <Route path='serviceProvidersMangement/providersMangement' element={<ProvidersMangement/>}></Route>
          <Route path='serviceProvidersMangement/facilitiesMangement' element={<FacilitiesMangement/>}></Route>
                      <Route path="chat" element={<Chat />} />

        </Route>):null}
        </Routes>
          </StoreProvider>

        </div>
        </div>
        <Footer/>
        </>
    );
}
export default Dashboard;