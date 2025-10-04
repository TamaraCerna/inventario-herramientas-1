import React, { useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useKeycloak } from "@react-keycloak/web";
import { useNavigate } from "react-router-dom";

// Importar imágenes
import img1 from "../img/trabajadores1.jpeg";
import img2 from "../img/trabajadores2.jpg";
import img3 from "../img/Trabsjsdores3.jpg";
import img4 from "../img/logoCat.png";
import img5 from "../img/ubermann.png";
import img6 from "../img/Makita-logo.png";
import img7 from "../img/baukerLogo.png";
import img8 from "../img/Bosch-Logo-2002-2018.png";
import img9 from "../img/Stanley-Logo.png";

const HeroSection: React.FC = () => {
  const { keycloak, initialized } = useKeycloak();
  const isLoggedIn = keycloak?.authenticated;
  const navigate = useNavigate();

  // 🚀 Redirigir automáticamente al home si el usuario ya está autenticado
  useEffect(() => {
    if (initialized && isLoggedIn) {
      navigate("/home");
    }
  }, [initialized, isLoggedIn, navigate]);

  if (!initialized) {
    return <div className="text-center mt-5">Cargando autenticación...</div>;
  }

  return (
    <div className="container-fluid mt-3 px-0">
      <div className="row g-0">
        {/* 🔹 Columna izquierda - Carrusel */}
        <div className="col-md-8">
          <div id="carouselExample" className="carousel slide" data-bs-ride="carousel">
            <div className="carousel-inner">
              <div className="carousel-item active">
                <img src={img1} className="d-block w-100 img-fluid" alt="Trabajadores 1" />
              </div>
              <div className="carousel-item">
                <img src={img2} className="d-block w-100 img-fluid" alt="Trabajadores 2" />
              </div>
              <div className="carousel-item">
                <img src={img3} className="d-block w-100 img-fluid" alt="Trabajadores 3" />
              </div>
            </div>

            {/* Controles del carrusel */}
            <button
              className="carousel-control-prev"
              type="button"
              data-bs-target="#carouselExample"
              data-bs-slide="prev"
            >
              <span className="carousel-control-prev-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Anterior</span>
            </button>
            <button
              className="carousel-control-next"
              type="button"
              data-bs-target="#carouselExample"
              data-bs-slide="next"
            >
              <span className="carousel-control-next-icon" aria-hidden="true"></span>
              <span className="visually-hidden">Siguiente</span>
            </button>
          </div>
        </div>

        {/* 🔹 Columna derecha - Login o mensaje */}
        <div className="col-md-4 d-flex align-items-center justify-content-center bg-light">
          <div className="p-5 text-center w-100" style={{ maxWidth: "400px" }}>
            {!isLoggedIn ? (
              <>
                <h3 className="fw-bold mb-4">
                  Bienvenido a <span style={{ color: "#fbbd08" }}>ToolRent</span>
                </h3>
                <p className="mb-4">
                  Accede a tu cuenta para gestionar préstamos, clientes y herramientas.
                </p>
                <button
                  className="btn btn-dark w-100 py-2 fw-bold"
                  onClick={() => keycloak.login()}
                >
                  Iniciar sesión con Keycloak
                </button>
              </>
            ) : (
              <>
                <h3 className="fw-bold mb-4">
                  Hola, {keycloak.tokenParsed?.preferred_username} 👋
                </h3>
                <p className="mb-4">Redirigiendo al panel principal...</p>
              </>
            )}
          </div>
        </div>
      </div>

      {/* 🔹 Barra inferior con logos */}
      <div style={{ backgroundColor: "#5f5a59" }} className="py-3 mt-3">
        <div className="d-flex justify-content-center flex-wrap align-items-center gap-4">
          {[img4, img5, img6, img7, img8, img9].map((logo, i) => (
            <img
              key={i}
              src={logo}
              alt={`logo-${i}`}
              className="brand-logo"
              style={{
                height: "60px",
                width: "auto",
                objectFit: "contain",
                filter: "brightness(0) invert(1)", // hace los logos visibles sobre fondo oscuro
              }}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default HeroSection;




