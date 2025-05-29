import Footer from "../Compoments/Footer";
import Header from "../Compoments/Header";
import SignupForm from "../Compoments/SignupForm";
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