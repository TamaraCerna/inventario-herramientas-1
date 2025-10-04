// src/service/reportsService.ts
import api from "./api"; // 👈 usamos la instancia CON TOKEN

const API_URL = "/reports"; // base relativa

// RF6.1 - Listar préstamos activos
export const getActiveLoans = async (state: string = "Activo") => {
  const response = await api.get(`${API_URL}/loans`, {
    params: { state },
  });
  return response.data;
};

// RF6.2 - Listar clientes con préstamos vencidos
export const getOverdueCustomers = async () => {
  const response = await api.get(`${API_URL}/overdue-customers`);
  return response.data;
};

// RF6.3 - Ranking de herramientas más usadas
export const getRankingTools = async () => {
  const response = await api.get(`${API_URL}/ranking-tools`);
  return response.data;
};

