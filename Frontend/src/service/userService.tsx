// src/service/userService.ts
import api from "./api"; // 👈 usamos la instancia con interceptor

const API_URL = "/users"; // base relativa; api ya tiene baseURL

// RF4.1 – Cambiar tarifa diaria
export const updateTariffDaily = async (
  userId: number,
  tariffId: number,
  day: number
) => {
  const response = await api.put(`${API_URL}/tariff/day`, null, {
    params: { userId, tariffId, day },
  });
  return response.data;
};

// RF4.2 – Cambiar multa diaria
export const updateFineDaily = async (
  userId: number,
  tariffId: number,
  fine: number
) => {
  const response = await api.put(`${API_URL}/tariff/fine`, null, {
    params: { userId, tariffId, fine },
  });
  return response.data;
};

// RF4.3 – Cambiar valor de reposición de herramienta
export const updateToolReplacementValue = async (
  userId: number,
  toolId: number,
  value: number
) => {
  const response = await api.put(`${API_URL}/tool/replacement`, null, {
    params: { userId, toolId, value },
  });
  return response.data;
};

// RF7.1 – Registrar usuario
export const registerUser = async (
  name: string,
  email: string,
  password: string
) => {
  const response = await api.post(`${API_URL}/register`, null, {
    params: { name, email, password },
  });
  return response.data;
};

// RF7.2 – Asignar rol a usuario
export const assignRole = async (id: number, role: string) => {
  const response = await api.put(`${API_URL}/${id}/role`, null, {
    params: { role },
  });
  return response.data;
};
