import { useState } from "react";
import { useNavigate } from "react-router-dom";
import NavbarUser from "../components/NavbarUser"; // <-- importa tu navbar

export default function HomeUser() {
  const username = localStorage.getItem("username") || "Usuario";
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const logout = () => {
    localStorage.removeItem("username");
    navigate("/");
  };

  return (
    <div>
      {/* Contenido de HomeUser */}
      <div style={{ padding: "20px", fontFamily: "Arial" }}>
        <h2>Bienvenido, {username}</h2>
      </div>
    </div>
  );
}

