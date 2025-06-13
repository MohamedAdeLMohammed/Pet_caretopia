import Footer from "../Compoments/Home/Footer";
import Header from "../Compoments/Home/Header";
import HomeBanner from "../Compoments/Home/HomeBanner";
import OurServices from "../Compoments/Home/OurServices";
import AboutUs from "../Compoments/Home/AboutUs";
import { Routes, Route } from 'react-router-dom';
import '../CSS/Home.css';
import PetServices from "../../Pet Owner/Components/PetMangement/PetServices";
import FAQSection from "../Compoments/Home/FAQSection";
import ClinicAppointments from "../../Pet Owner/Components/Appointments/ClinicAppointments";
import HotelAppointments from "../../Pet Owner/Components/Appointments/HotelAppointments";
import TrainingCenterAppointments from "../../Pet Owner/Components/Appointments/TrainingCenterAppointments";
import StorePage from "../Compoments/Store/StorePage";
import ToysProductsDetails from "../Compoments/Store/Toys";
import MedictionsProductsDetails from "../Compoments/Store/Pharmacy"
import AcessoriesProductsDetails from "../Compoments/Store/AccessoriesDetails"
import FoodProductsDetails from "../Compoments/Store/FoodProductsDetails";
import AdoptationOffers from "../../Pet Owner/Components/PetMangement/AdoptationOffers";
import ShelterMangement from "../../Admin/Components/ShelterMangement/ShelterMangement";
import PetRecognition from "../../Pet Owner/Components/PetMangement/Pet Recoginition";
function Home() {
    return (
        <div className="app-wrapper d-flex flex-column min-vh-100">
            <Header />
            <main className="flex-fill">
                <Routes>
                    <Route path="/" element={
                        <>
                            <HomeBanner />
                            <OurServices />
                            <AboutUs />
                            <FAQSection />
                        </>
                    } />
                    <Route path="store" element={<StorePage />} />
                    <Route path="petServices" element={<PetServices />} />
                    <Route path="petServices/clinics" element={<ClinicAppointments />} />
                    <Route path="petServices/hotels" element={<HotelAppointments />} />
                    <Route path="petServices/trainingCenter" element={<TrainingCenterAppointments />} />
                    <Route path="petServices/trainingCenter" element={<TrainingCenterAppointments />} />
                    <Route path="store/TOYSProductsDetails" element={<ToysProductsDetails />} />
                    <Route path="store/MEDICATIONSProductsDetails" element={<MedictionsProductsDetails />} />
                    <Route path="store/ACCESSORIESProductsDetails" element={<AcessoriesProductsDetails />} />
                    <Route path="store/FoodProductsDetails" element={<FoodProductsDetails />} />
                    <Route path="petRecogintion" element={<PetRecognition />} />
                    <Route path="adoption" element={<AdoptationOffers />} />
                    <Route path="shelters" element={<ShelterMangement />} />
                </Routes>
            </main>
            <Footer />
        </div>
    );
}

export default Home;