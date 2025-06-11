import Footer from "../Compoments/Home/Footer";
import Header from "../Compoments/Home/Header";
import SignupForm from "../Compoments/Auth/SignupForm";
import '../CSS/Home.css'
import '../CSS/Signup.css'
function Signup(){
    return(
        <>
        <div className="app-wrapper d-flex flex-column min-vh-100">
        <Header/>
        <main className="flex-fill">
        <SignupForm/>
        
        </main>
        <Footer/>
        </div>
        </>
    );
}
export default Signup;