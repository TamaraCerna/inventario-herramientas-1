// src/service/loanActionsService.ts
import api from "./api"; // 👈 usamos la instancia con interceptor (con token)

const API_URL = "/loans";

// Obtener préstamos activos de un cliente
export const getActiveLoansByClient = async (clientId: number) => {
  const response = await api.get(`${API_URL}/active`, {
    params: { clientId },
  });
  return response.data;
};

// Registrar devolución de una herramienta
export const returnTool = async (
  loanId: number,
  toolId: number,
  userId: number
) => {
  const response = await api.put(
    `${API_URL}/${loanId}/return`,
    null, // sin body
    {
      params: { toolId, userId },
      responseType: "text", // Spring puede devolver solo texto
    }
  );
  return response.data;
};

// Calcular multa
export const calculateFine = async (loanId: number) => {
  try {
    const response = await api.put(`${API_URL}/${loanId}/fine`);
    return response.data; // LoanEntity actualizado
  } catch (error: any) {
    console.error("❌ Error en calculateFine:", error);
    const backendMessage =
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.response?.data ||
      "Error al calcular multa";
    throw new Error(backendMessage);
  }
};


