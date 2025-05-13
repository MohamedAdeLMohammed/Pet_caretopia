import Footer from "../Compoments/Footer";
import Header from "../Compoments/Header";
import HomeBanner from "../Compoments/HomeBanner";
import OurServices from "../Compoments/OurServices";
import '../CSS/Home.css'
function Home(){
    return(
        <>
        <div className="app-wrapper d-flex flex-column min-vh-100">
        <Header/>
        <main className="flex-fill">
        <HomeBanner/>
        <OurServices/>
        </main>
        <Footer/>
        </div>
        </>
    );
}
export default Home;