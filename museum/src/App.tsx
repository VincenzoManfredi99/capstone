import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import MyNavbar from "./components/MyNavbar";
import PaginaInfo from "./components/PaginaInfo";
import { BrowserRouter, Route, Routes } from "react-router";
import Login from "./components/Login";
import Registrazione from "./components/Registrazione";
import Home from "./components/Home";
import Museo from "./components/Museo";
import Sala from "./components/Sala";
import Scena from "./components/Scena";

function App() {
  return (
    <>
      <BrowserRouter>
        <MyNavbar />
        <div>
          <Routes>
            <Route path="/home" element={<Home />} />
            <Route path="/info" element={<PaginaInfo />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registrazione" element={<Registrazione />} />
            <Route path="/museo/*" element={<Museo />} />
            <Route path="/sala" element={<Sala />} />
            <Route path="/scena" element={<Scena />} />
          </Routes>
        </div>
      </BrowserRouter>
    </>
  );
}

export default App;
