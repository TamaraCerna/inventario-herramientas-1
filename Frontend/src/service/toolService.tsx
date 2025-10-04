// src/service/toolService.ts
import api from "./api"; // 👈 IMPORTANTE: usamos la instancia con interceptor

// Interfaz única de herramienta
export interface Tool {
  idTool: number;                // en la respuesta del backend siempre viene
  nameTool: string;
  categoryTool: string;
  replacementValueTool: number;
  userId?: number;
}

// RF1.1 Registrar nueva herramienta (POST /tools/register)
export const registerTool = async (tool: Tool) => {
  const response = await api.post("/tools/register", null, {
    params: {
      name_tool: tool.nameTool,
      category_tool: tool.categoryTool,
      replacement_value_tool: tool.replacementValueTool,
      userId: tool.userId,
    },
  });
  return response.data;
};

// RF1.2 Obtener lista de herramientas (GET /tools)
export const getTools = async (): Promise<Tool[]> => {
  const response = await api.get<Tool[]>("/tools");
  console.log("🔧 Herramientas cargadas desde backend:", response.data);
  return response.data; // 👈 devolvemos solo el array
};

// RF1.3 Eliminar herramienta (DELETE /tools/{id})
export const deleteTool = async (toolId: number): Promise<string> => {
  try {
    const response = await api.delete(`/tools/${toolId}`);
    console.log("🗑️ Respuesta delete:", response.status, response.data);

    // Si tu backend devuelve algo en el body:
    if (response.data && typeof response.data === "string") {
      return `✅ ${response.data}`;
    }

    // Si es 204 No Content o similar:
    return "✅ Herramienta eliminada correctamente";
  } catch (error: any) {
    console.error("❌ Error al eliminar herramienta:", error);
    if (error.response && error.response.data) {
      return `Error: ${error.response.data}`;
    }
    return `Error: ${error.message}`;
  }
};

