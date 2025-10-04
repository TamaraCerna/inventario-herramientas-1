import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import type { ReactNode } from "react";
import RegisterTool from "./components/RegisterTool";
import UpdateTool from "./components/UpdateTool";
import UpdateStock from "./components/UpdateStock";
import DeleteTool from "./components/DeleteTools";


import Layout from "./components/Layout";
import HeroSection from "./components/HeroSection";

// 🔒 Ruta privada con roles
interface PrivateRouteProps {
  children: ReactNode;
  roles?: string[];
}

const PrivateRoute = ({ children, roles = [] }: PrivateRouteProps) => {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized)
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        Cargando autenticación...
      </div>
    );

  if (!keycloak.authenticated) {
    keycloak.login();
    return null;
  }

  const realmRoles: string[] = keycloak?.tokenParsed?.realm_access?.roles || [];
  const hasRole =
    roles.length === 0 || roles.some((r) => realmRoles.includes(r));

  if (!hasRole)
    return (
      <h2 style={{ textAlign: "center", marginTop: "40px" }}>
        Acceso denegado
      </h2>
    );

  return <>{children}</>;
};

// 🚀 App principal
const App: React.FC = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          {/* 🏠 Página pública */}
          <Route path="/" element={<HeroSection />} />

          {/* 👤 Home privado */}
          <Route
            path="/home"
            element={
              <PrivateRoute roles={["USER", "ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h1>Bienvenido al Sistema</h1>
                  <p>Has iniciado sesión correctamente 🎉</p>
                </div>
              </PrivateRoute>
            }
          />

          {/* ⚙️ Panel de administración */}
          <Route
            path="/admin"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Panel de administración</h2>
                  <p>Solo los administradores pueden acceder a esta sección.</p>
                </div>
              </PrivateRoute>
            }
          />

          {/* 🛠 Herramientas */}
          <Route
            path="/registertool"
            element={
              <PrivateRoute roles={["USER", "ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Panel de administración</h2>
                  <p>Solo los administradores pueden acceder a esta sección.</p>
                </div>
              </PrivateRoute>
            }
          />

          <Route
            path="/updatetool"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <UpdateTool />
              </PrivateRoute>
            }
          />

          <Route
            path="/deletetool"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <DeleteTool />
              </PrivateRoute>
            }
          />

          {/* 💼 Préstamos */}
          <Route
            path="/registerloan"
            element={
              <PrivateRoute roles={["USER", "ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Panel de administración</h2>
                  <p>Solo los administradores pueden acceder a esta sección.</p>
                </div>
              </PrivateRoute>
            }
          />

          <Route
            path="/returnloan"
            element={
              <PrivateRoute roles={["USER", "ADMIN"]}>
                <UpdateStock />
              </PrivateRoute>
            }
          />

          {/* 🧾 Reportes */}
          <Route
            path="/report/loans"
            element={
              <PrivateRoute roles={["ADMIN", "USER"]}>
                <RegisterTool />
              </PrivateRoute>
            }
          />

          <Route
            path="/report/overdue-customers"
            element={
              <PrivateRoute roles={["ADMIN", "USER"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Clientes con atraso</h2>
                </div>
              </PrivateRoute>
            }
          />

          <Route
            path="/report/ranking-tools"
            element={
              <PrivateRoute roles={["ADMIN", "USER"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Ranking de herramientas</h2>
                </div>
              </PrivateRoute>
            }
          />

          {/* 🧍 Clientes */}
          <Route
            path="/registerclient"
            element={
              <PrivateRoute roles={["ADMIN", "USER"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Registrar cliente</h2>
                </div>
              </PrivateRoute>
            }
          />

          {/* 🧍 Empleados */}
          <Route
            path="/registeremployee"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Registrar empleado</h2>
                </div>
              </PrivateRoute>
            }
          />

          {/* 🧾 Kardex */}
          <Route
            path="/homekardex"
            element={
              <PrivateRoute roles={["ADMIN"]}>
                <div style={{ textAlign: "center", marginTop: "40px" }}>
                  <h2>Kardex</h2>
                </div>
              </PrivateRoute>
            }
          />

          {/* ❌ Página no encontrada */}
          <Route
            path="*"
            element={
              <h2 style={{ textAlign: "center", marginTop: "40px" }}>
                Página no encontrada
              </h2>
            }
          />
        </Routes>
      </Layout>
    </Router>
  );
};

export default App;





