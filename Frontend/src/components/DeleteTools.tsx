import React, { useEffect, useState } from "react";
import { getTools, deleteTool } from "../service/toolService";
import type { Tool } from "../service/toolService";


const ToolDropdown: React.FC = () => {
  const [tools, setTools] = useState<Tool[]>([]);
  const [selectedTool, setSelectedTool] = useState<string>("");
  const [message, setMessage] = useState<string>("");

  // 🔹 Cargar herramientas al iniciar
  useEffect(() => {
    const fetchTools = async () => {
      try {
        const data = await getTools();
        setTools(data);
      } catch (error) {
        console.error(error);
        setMessage("❌ Error al cargar herramientas");
      }
    };
    fetchTools();
  }, []);

  // 🔹 Manejo de eliminación
  const handleDelete = async () => {
    if (!selectedTool) return;

    try {
      const responseMsg = await deleteTool(Number(selectedTool));

      // Actualizar lista de herramientas eliminando la seleccionada
      setTools((prev) => prev.filter((t) => t.idTool !== Number(selectedTool)));

      // Resetear selección
      setSelectedTool("");

      // Mensaje de éxito / error desde el servicio
      setMessage(responseMsg);
    } catch (error: any) {
      console.error(error);
      setMessage("❌ Error inesperado al eliminar la herramienta");
    }
  };

  return (
    <div style={{ padding: "1rem" }}>
      <label htmlFor="tool" className="form-label">
        Selecciona una herramienta
      </label>

      <select
        id="tool"
        className="form-select"
        value={selectedTool}
        onChange={(e) => setSelectedTool(e.target.value)}
      >
        <option value="">-- Selecciona --</option>
        {tools.length > 0 ? (
          tools.map((tool) => (
            <option key={tool.idTool} value={tool.idTool}>
              {tool.nameTool} ({tool.categoryTool})
            </option>
          ))
        ) : (
          <option disabled>⚠️ No hay herramientas disponibles</option>
        )}
      </select>

      <button
        onClick={handleDelete}
        disabled={!selectedTool}
        style={{
          marginTop: "1rem",
          background: "red",
          color: "white",
          border: "none",
          padding: "0.5rem 1rem",
          borderRadius: "6px",
          cursor: selectedTool ? "pointer" : "not-allowed",
        }}
      >
        Eliminar herramienta
      </button>

      {message && (
        <p
          style={{
            marginTop: "1rem",
            fontWeight: "bold",
            color: message.startsWith("✅") ? "green" : "red",
          }}
        >
          {message}
        </p>
      )}
    </div>
  );
};

export default ToolDropdown;


