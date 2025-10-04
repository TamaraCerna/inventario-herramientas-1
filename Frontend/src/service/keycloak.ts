import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "sisgr",
  clientId: "sisgr-front",
});

(window as any).keycloak = keycloak; // 👈 esto permite inspeccionarlo desde consola
export default keycloak;



