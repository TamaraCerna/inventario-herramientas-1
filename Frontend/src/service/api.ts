import axios, { type AxiosRequestHeaders, type InternalAxiosRequestConfig } from "axios";
import type { KeycloakInstance } from "keycloak-js";

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8082";

console.log("🌐 BASE_URL que está usando Axios:", BASE_URL);

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const setAxiosInterceptors = (keycloak: KeycloakInstance) => {
  api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    console.log("🛰 Interceptor de Axios ejecutado para:", config.url);

    if (keycloak?.token) {
      // Si no hay headers, creamos uno tipado correctamente
      if (!config.headers) {
        config.headers = {} as AxiosRequestHeaders;
      }

      // Ahora sí, seteamos Authorization
      (config.headers as AxiosRequestHeaders).Authorization = `Bearer ${keycloak.token}`;

      console.log(
        "🔑 Token añadido al header Authorization:",
        keycloak.token.substring(0, 15) + "..."
      );
    } else {
      console.warn("⚠️ No se encontró token de Keycloak en el interceptor");
    }

    return config;
  });
};

export default api;
