import axios from "axios";

// Tomamos la URL del backend desde las variables de entorno de Vite
const API_BASE = import.meta.env.VITE_API_URL;

export const getToolCategories = async (): Promise<string[]> => {
  const response = await axios.get(`${API_BASE}/enums/tools`);
  return response.data;
};

export const getToolStates = async (): Promise<string[]> => {
  const response = await axios.get(`${API_BASE}/enums/states`);
  return response.data;
};

