import React, { useEffect, useState } from "react";
import { getStockTools, updateTariffDay } from "../service/modifytariffService";

interface StockTool {
  id: number;
  stockTool: string;
  stockQuantity: number;
  tariffDaily: number;
}

const ModifyTariff: React.FC = () => {
  const [tools, setTools] = useState<StockTool[]>([]);
  const [selectedTool, setSelectedTool] = useState<number | null>(null);
  const [newTariff, setNewTariff] = useState<number>(0);

  const userId = Number(localStorage.getItem("userId")); // 👈 ID del usuario logueado

  // cargar herramientas al montar
  useEffect(() => {
    const fetchTools = async () => {
      try {
        const data = await getStockTools();
        setTools(data);
      } catch (err) {
        console.error(err);
        alert("❌ Error al cargar herramientas");
      }
    };
    fetchTools();
  }, []);

  const handleUpdate = async () => {
    if (!selectedTool) {
      alert("Debes seleccionar una herramienta");
      return;
    }
    try {
      const msg = await updateTariffDay(userId, selectedTool, newTariff);
      alert(msg);
    } catch (err) {
      console.error(err);
      alert("❌ No se pudo actualizar la tarifa");
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "500px" }}>
      <h3>Modificar Tarifa Diaria</h3>

      <label>Selecciona herramienta:</label>
      <select
        className="form-control mb-3"
        onChange={(e) => setSelectedTool(Number(e.target.value))}
      >
        <option value="">-- Selecciona --</option>
        {tools.map((tool) => (
          <option key={tool.id} value={tool.id}>
            {tool.stockTool} (Tarifa actual: {tool.tariffDaily})
          </option>
        ))}
      </select>

      <label>Nueva tarifa diaria:</label>
      <input
        type="number"
        className="form-control mb-3"
        value={newTariff}
        onChange={(e) => setNewTariff(Number(e.target.value))}
      />

      <button className="btn btn-success w-100" onClick={handleUpdate}>
        Actualizar Tarifa
      </button>
    </div>
  );
};

export default ModifyTariff;
