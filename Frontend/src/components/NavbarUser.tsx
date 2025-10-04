import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

const NavbarUser: React.FC = () => {
  const { keycloak } = useKeycloak();
  const isLoggedIn = keycloak?.authenticated;
  const roles = keycloak?.tokenParsed?.realm_access?.roles || [];
  const navigate = useNavigate();

  const handleLogout = () => {
    keycloak.logout({ redirectUri: "http://localhost:5173" });
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark w-100">
      <div className="container-fluid">

        {/* 🔹 Marca o título */}
        <a
          className="navbar-brand fw-bold ms-3"
          style={{ cursor: "pointer" }}
          onClick={() => navigate("/home")}
        >
          SISGR
        </a>

        {/* 🔹 Botón toggle para pantallas pequeñas */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* 🔹 Secciones de navegación */}
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav ms-auto me-3 d-flex gap-4">

            {/* Herramientas */}
            <li className="nav-item dropdown">
              <a
                className="nav-link fs-6 dropdown-toggle"
                href="#"
                id="herramientasDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Herramientas
              </a>
              <ul className="dropdown-menu" aria-labelledby="herramientasDropdown">
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/registertool")}>
                    Registrar herramienta
                  </a>
                </li>
                {/* 🔸 Solo ADMIN puede editar o eliminar */}
                {roles.includes("ADMIN") && (
                  <>
                    <li>
                      <a className="dropdown-item" onClick={() => navigate("/updatetool")}>
                        Editar valor
                      </a>
                    </li>
                    <li>
                      <a className="dropdown-item" onClick={() => navigate("/deletetool")}>
                        Eliminar herramienta
                      </a>
                    </li>
                  </>
                )}
              </ul>
            </li>

            {/* Préstamos */}
            <li className="nav-item dropdown">
              <a
                className="nav-link fs-6 dropdown-toggle"
                href="#"
                id="prestamosDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Préstamos
              </a>
              <ul className="dropdown-menu" aria-labelledby="prestamosDropdown">
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/registerloan")}>
                    Registrar préstamo
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/returnloan")}>
                    Registrar devolución
                  </a>
                </li>
                {/* 🔸 Solo ADMIN puede modificar tarifas */}
                {roles.includes("ADMIN") && (
                  <>
                    <li>
                      <a className="dropdown-item" onClick={() => navigate("/modifyloan")}>
                        Tarifa diaria
                      </a>
                    </li>
                    <li>
                      <a className="dropdown-item" onClick={() => navigate("/modifyfine")}>
                        Valor multa
                      </a>
                    </li>
                  </>
                )}
              </ul>
            </li>

            {/* Clientes */}
            <li className="nav-item dropdown">
              <a
                className="nav-link fs-6 dropdown-toggle"
                href="#"
                id="clientesDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Clientes
              </a>
              <ul className="dropdown-menu" aria-labelledby="clientesDropdown">
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/registerclient")}>
                    Registrar cliente
                  </a>
                </li>
              </ul>
            </li>

            {/* Reportes */}
            <li className="nav-item dropdown">
              <a
                className="nav-link fs-6 dropdown-toggle"
                href="#"
                id="reportesDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Reportes
              </a>
              <ul className="dropdown-menu" aria-labelledby="reportesDropdown">
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/report/loans")}>
                    Préstamos activos
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/report/overdue-customers")}>
                    Clientes con atraso
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" onClick={() => navigate("/report/ranking-tools")}>
                    Ranking de herramientas
                  </a>
                </li>
              </ul>
            </li>

            {/* 🔸 Solo ADMIN ve Kardex y Empleados */}
            {roles.includes("ADMIN") && (
              <>
                <li className="nav-item">
                  <a className="nav-link fs-6" onClick={() => navigate("/homekardex")}>
                    Kardex
                  </a>
                </li>
                <li className="nav-item dropdown">
                  <a
                    className="nav-link fs-6 dropdown-toggle"
                    href="#"
                    id="empleadosDropdown"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    Empleados
                  </a>
                  <ul className="dropdown-menu" aria-labelledby="empleadosDropdown">
                    <li>
                      <a className="dropdown-item" onClick={() => navigate("/registeremployee")}>
                        Registrar empleado
                      </a>
                    </li>
                  </ul>
                </li>
              </>
            )}
          </ul>
        </div>

        {/* 🔹 Lado derecho: usuario + logout */}
        {isLoggedIn && (
          <div className="d-flex align-items-center me-3">
            <span className="text-light me-3">
              {keycloak.tokenParsed?.preferred_username}
            </span>
            <button onClick={handleLogout} className="btn btn-warning fw-bold">
              Cerrar sesión
            </button>
          </div>
        )}
      </div>

      {/* 🔹 CSS: mantener dropdown abierto al hover */}
      <style>{`
        .dropdown:hover .dropdown-menu {
          display: block;
          margin-top: 0;
        }
        a.dropdown-item {
          cursor: pointer;
        }
      `}</style>
    </nav>
  );
};

export default NavbarUser;

