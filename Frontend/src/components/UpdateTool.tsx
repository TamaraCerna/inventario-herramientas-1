import React, { useEffect, useState } from "react";
import axios from "axios";
import { updateToolReplacementValue } from "../service/userService";

interface Tool {
  idTool: number;
  nameTool: string;
  replacementValueTool: number;
}

const UpdateToolReplacement: React.FC = () => {
  const [tools, setTools] = useState<Tool[]>([]);
  const [selectedTool, setSelectedTool] = useState<number | null>(null);
  const [value, setValue] = useState<number>(0);
  const [message, setMessage] = useState<string>("");

  const userId = Number(localStorage.getItem("userId")); // ⚡ Usuario logueado

  // Cargar herramientas desde backend
  useEffect(() => {
    axios
      .get('${import.meta.env.VITE_API_URL}/users/modify/tools')
      .then((res) => setTools(res.data))
      .catch((err) => console.error("Error al cargar herramientas:", err));
  }, []);

  const handleUpdate = async () => {
    if (!selectedTool) {
      alert("⚠️ Debes seleccionar una herramienta.");
      return;
    }
    try {
      const msg = await updateToolReplacementValue(userId, selectedTool, value);
      setMessage("✅ " + msg);
      setValue(0);
      setSelectedTool(null);
    } catch (err) {
      console.error(err);
      setMessage("❌ Error al actualizar el valor de reposición.");
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "500px" }}>
      <h3>Modificar Valor de Reposición (RF4.3)</h3>

      {/* Dropdown con herramientas */}
      <label className="form-label">Herramienta</label>
      <select
        className="form-select mb-3"
        value={selectedTool ?? ""}
        onChange={(e) => setSelectedTool(Number(e.target.value))}
      >
        <option value="">-- Selecciona una herramienta --</option>
        {tools.map((tool) => (
          <option key={tool.idTool} value={tool.idTool}>
            {tool.nameTool} (Reposición actual: {tool.replacementValueTool})
          </option>
        ))}
      </select>

      {/* Input nuevo valor */}
      <label className="form-label">Nuevo valor de reposición</label>
      <input
        type="number"
        className="form-control mb-3"
        value={value}
        onChange={(e) => setValue(Number(e.target.value))}
      />

      <button className="btn btn-warning w-100" onClick={handleUpdate}>
        Actualizar Valor
      </button>

      {message && <p className="mt-3">{message}</p>}
    </div>
  );
};

export default UpdateToolReplacement;
