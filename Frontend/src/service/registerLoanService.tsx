// src/service/loanService.ts
import api from "./api";

const API_BASE = "/loans";

export async function createLoan(
  clientId: number,
  toolId: number,
  initDate: string,
  finishDate: string
) {
  try {
    const response = await api.post(`${API_BASE}/create`, null, {
      params: {
        clientId,
        toolId,
        initDate,
        finishDate,
      },
    });

    return response.data;
  } catch (error: any) {
    console.error("❌ Error al registrar préstamo:", error);

    // Manejo de error similar al que hiciste con fetch
    const backendMsg =
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.response?.data ||
      "Error al registrar préstamo";

    throw new Error(backendMsg);
  }
}





