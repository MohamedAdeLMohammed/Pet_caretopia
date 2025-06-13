import './App.css'
import Dashboard from './Shared/Pages/Dashboard'
import Home from './Shared/Pages/Home'
import { Routes , Route } from 'react-router-dom'
import Login from './Shared/Pages/Login'
import Signup from './Shared/Pages/Signup'
import ResetPassword from './Shared/Pages/ResetPassword'
import PetServices from './Pet Owner/Components/PetMangement/PetServices'
import Header from './Shared/Compoments/Home/Header'
import Footer from './Shared/Compoments/Home/Footer'

function App() {

  return (
    <>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/home/*" element={<Home />} /> 
      <Route path='/login' element={<Login/>}></Route>
      <Route path='/signup' element={<Signup/>}></Route>
      <Route path='/reset-password' element={<ResetPassword/>}></Route>
      <Route path='/dashboard/*' element={<Dashboard/>}></Route>
    </Routes>
    {/* <Dashboard/> */}
    </>
  )
}

export default App
