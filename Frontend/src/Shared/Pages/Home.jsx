import Footer from "../Compoments/Home/Footer";
import Header from "../Compoments/Home/Header";
import HomeBanner from "../Compoments/Home/HomeBanner";
import OurServices from "../Compoments/Home/OurServices";
import AboutUs from "../Compoments/Home/AboutUs";

import '../CSS/Home.css'
import FAQSection from "../Compoments/Home/FAQSection";
function Home(){
    return(
        <>
        <div className="app-wrapper d-flex flex-column min-vh-100">
        <Header/>
        <main className="flex-fill">
        <HomeBanner/>
        <OurServices/>
        <AboutUs />
        <FAQSection/>
        </main>
        <Footer/>
        </div>
        </>
    );
}
export default Home;