import { createRoot } from "react-dom/client";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import keycloak from "./service/keycloak";
import App from "./App";
import { setAxiosInterceptors } from "./service/api"; // 👈 importante

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./index.css";

createRoot(document.getElementById("root")!).render(
  <ReactKeycloakProvider
    authClient={keycloak}
    initOptions={{
      onLoad: "login-required",
      checkLoginIframe: false,
      redirectUri: window.location.origin + "/home",
    }}
    onTokens={(tokens) => {
      // 🔑 Cada vez que Keycloak actualice o refresque el token, lo agregamos a Axios
      setAxiosInterceptors(keycloak);
      console.log("🔄 Token actualizado y aplicado a Axios");
    }}
  >
    <App />
  </ReactKeycloakProvider>
);






