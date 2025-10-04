import axios from "axios";

const API_URL = `${import.meta.env.VITE_API_URL}/kardex`;

// RF5.2 – Historial de herramienta
export const getToolHistory = async (toolId: number) => {
  const response = await axios.get(`${API_URL}/history/${toolId}`);
  return response.data;
};

// RF5.3 – Búsqueda por rango de fechas
export const searchKardex = async (init: string, end: string) => {
  const response = await axios.get(`${API_URL}/search`, {
    params: { init, end },
  });
  return response.data;
};

