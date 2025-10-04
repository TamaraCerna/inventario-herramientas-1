// src/service/stockToolsService.ts (por ejemplo)
import api from "./api";

const API_URL = "/users";

// Obtener todas las herramientas con tarifa
export const getStockTools = async () => {
  const response = await api.get(`${API_URL}/modify`);
  return response.data; // List<StockToolsEntity>
};

// Modificar tarifa diaria
export const updateTariffDay = async (
  userId: number,
  toolId: number,
  day: number
) => {
  const response = await api.put(`${API_URL}/tariff/day`, null, {
    params: { userId, tariffId: toolId, day },
  });
  return response.data;
};

