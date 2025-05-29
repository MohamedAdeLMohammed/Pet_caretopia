import Footer from "../Compoments/Footer";
import Header from "../Compoments/Header";
import LoginForm from "../Compoments/LoginForm";
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