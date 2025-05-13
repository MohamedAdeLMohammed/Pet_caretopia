import './App.css'
import Swal from 'sweetalert2'
import Header from './Shared/Compoments/Header'
import HomeBanner from './Shared/Compoments/HomeBanner'
import Dashboard from './Shared/Pages/Dashboard'
import Home from './Shared/Pages/Home'
import { Routes , Route } from 'react-router-dom'
import Login from './Shared/Pages/Login'
import Signup from './Shared/Pages/Signup'
import ResetPassword from './Shared/Pages/ResetPassword'


function App() {

  return (
    <>
    <Routes>
      <Route path="/" element={<Home />} /> 
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
