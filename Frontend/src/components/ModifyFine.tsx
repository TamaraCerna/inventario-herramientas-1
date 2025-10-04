import React, { useEffect, useState } from "react";
import axios from "axios";
import { updateFineDaily } from "../service/userService";

interface StockTool {
  id: number;
  stockTool: string;
  stockQuantity: number;
  tariffDaily: number;
  fineDaily: number;
}

const ModifyFine: React.FC = () => {
  const [tools, setTools] = useState<StockTool[]>([]);
  const [selectedTool, setSelectedTool] = useState<number | null>(null);
  const [fine, setFine] = useState<number>(0);

  const userId = Number(localStorage.getItem("userId"));

  // Cargar herramientas
 useEffect(() => {
  axios
    .get(`${import.meta.env.VITE_API_URL}/users/modify`)
    .then((res) => setTools(res.data))
    .catch((err) => console.error("Error al cargar herramientas:", err));
}, []);


  const handleUpdate = async () => {
    if (!selectedTool) {
      alert("⚠️ Debes seleccionar una herramienta.");
      return;
    }
    try {
      const msg = await updateFineDaily(userId, selectedTool, fine);
      alert("✅ " + msg);
      setFine(0);
      setSelectedTool(null);
    } catch (err) {
      console.error(err);
      alert("❌ No se pudo actualizar la multa.");
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "500px" }}>
      <h3>Modificar Multa Diaria (RF4.2)</h3>

      <label className="form-label">Herramienta</label>
      <select
        className="form-select mb-3"
        value={selectedTool ?? ""}
        onChange={(e) => setSelectedTool(Number(e.target.value))}
      >
        <option value="">-- Selecciona una herramienta --</option>
        {tools.map((tool) => (
          <option key={tool.id} value={tool.id}>
            {tool.stockTool} (Tarifa: {tool.tariffDaily}, Multa actual: {tool.fineDaily})
          </option>
        ))}
      </select>

      <label className="form-label">Nueva multa diaria</label>
      <input
        type="number"
        className="form-control mb-3"
        placeholder="Ej: 2500"
        value={fine}
        onChange={(e) => setFine(Number(e.target.value))}
      />

      <button className="btn btn-danger w-100" onClick={handleUpdate}>
        Actualizar Multa
      </button>
    </div>
  );
};

export default ModifyFine;

