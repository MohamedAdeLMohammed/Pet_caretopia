import Footer from "../Compoments/Home/Footer";
import Header from "../Compoments/Home/Header";
import LoginForm from "../Compoments/Auth/LoginForm";
import '../CSS/Home.css'
import '../CSS/Login.css'

function Login(){
    return(
        <>
        <div className="app-wrapper d-flex flex-column min-vh-100">
        <Header/>
        <main className="flex-fill">
        <LoginForm/>
        </main>
        <Footer/>
        </div>
        </>
    );
}
export default Login;