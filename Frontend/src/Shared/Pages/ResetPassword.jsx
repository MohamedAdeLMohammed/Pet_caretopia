import Footer from "../Compoments/Home/Footer";
import Header from "../Compoments/Home/Header";
import ResetPasswordForm from "../Compoments/Auth/ResetPasswordForm";
import '../CSS/Home.css'
import '../CSS/Login.css'

function ResetPassword(){
    return(
        <>
        <div className="app-wrapper d-flex flex-column min-vh-100">
        <Header/>
        <main className="flex-fill">
        <ResetPasswordForm/>
        </main>
        <Footer/>
        </div>
        </>
    );
}
export default ResetPassword;