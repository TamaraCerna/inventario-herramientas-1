import type { ReactNode } from "react";
import { useLocation } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";

import Navbar from "./Navbar";
import NavbarUser from "./NavbarUser";

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const location = useLocation();
  const { keycloak } = useKeycloak();

  // 🔹 Definir las rutas del panel de usuario / admin
  const userRoutes = [
  "/home",
  "/admin", // 👈 agrega esto
  "/homekardex",
  "/registertool",
  "/registerloan",
  "/registerclient",
  "/home-report",
  "/returnloan",
  "/modifyloan",
  "/modifyfine",
  "/updatetool",
  "/report/loans",
  "/report/overdue-customers",
  "/report/ranking-tools",
  "/registeremployee",
  "/deletetool"
];


  // 🔹 Detectar si la ruta actual pertenece al panel
  const isUserRoute = userRoutes.some((r) =>
    location.pathname.toLowerCase().startsWith(r)
  );

  // 🔹 Render según tipo de ruta
  return (
    <>
      {isUserRoute ? <NavbarUser /> : <Navbar />}
      <main>{children}</main>
    </>
  );
};

export default Layout;

