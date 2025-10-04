import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import logo from "../img/logoTool.png";
import { useKeycloak } from "@react-keycloak/web";

const Navbar: React.FC = () => {
  const { keycloak } = useKeycloak();
  const isLoggedIn = keycloak?.authenticated;
  const roles: string[] = keycloak?.tokenParsed?.realm_access?.roles || [];

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark w-100 shadow-sm">
      <div className="container-fluid">
        {/* Logo ToolRent */}
        <a href="/" className="me-auto ms-5 d-flex align-items-center">
          <img
            src={logo}
            alt="ToolRent Logo"
            className="img-fluid"
            style={{ height: "60px", width: "auto" }}
          />
        </a>

        {/* Links principales */}
        <div className="collapse navbar-collapse d-flex justify-content-end align-items-center">
          <ul className="navbar-nav d-flex align-items-center me-3">
            <li className="nav-item">
              <a className="nav-link fs-6" href="#">
                Quiénes somos
              </a>
            </li>
            <li className="nav-item separator">
              <a className="nav-link fs-6" href="#">
                Servicio
              </a>
            </li>
            <li className="nav-item separator">
              <a className="nav-link fs-6" href="#">
                Contacto
              </a>
            </li>

            {/* Mostrar solo si el usuario está logueado */}
            {isLoggedIn && (
              <>
                <li className="nav-item separator">
                  <a className="nav-link fs-6" href="/home">
                    Inicio
                  </a>
                </li>

                {/* Solo visible para ADMIN */}
                {roles.includes("ADMIN") && (
                  <li className="nav-item separator">
                    <a className="nav-link fs-6" href="/admin">
                      Panel Admin
                    </a>
                  </li>
                )}
              </>
            )}
          </ul>

          {/* 🔸 Zona derecha: usuario + botón */}
          <div className="d-flex align-items-center me-5">
            {isLoggedIn ? (
              <>
                <span className="text-light me-3">
                  Bienvenido,{" "}
                  <strong>{keycloak.tokenParsed?.preferred_username}</strong>
                </span>
                <button
                  className="btn btn-warning fw-bold"
                  onClick={() =>
                    keycloak.logout({ redirectUri: "http://localhost:5173" })
                  }
                >
                  Cerrar sesión
                </button>
              </>
            ) : (
              <button
                className="btn btn-warning fw-bold"
                onClick={() => keycloak.login()}
              >
                Iniciar sesión
              </button>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;






