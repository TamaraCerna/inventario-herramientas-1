// src/service/clientService.ts
import api from "./api"; // 👈 usamos axios con interceptor

export const registerClient = async (
  name: string,
  rut: string,
  phone: number,
  email: string
) => {
  const response = await api.post("/clients/register", null, {
    params: { name, rut, phone, email },
  });

  return response.data;
};
