import { useState } from "react";
import axios from "axios";

interface Kardex {
  id: number;
  date: string;
  description: string;
  quantity: number;
}

export default function HomeKardex() {
  const username = localStorage.getItem("username") || "Usuario";

  const [initDate, setInitDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [toolId, setToolId] = useState("");
  const [results, setResults] = useState<Kardex[]>([]);
  const [loading, setLoading] = useState(false);

  const API_URL = '${import.meta.env.VITE_API_URL}/kardex';

  const logout = () => {
    localStorage.removeItem("username");
    window.location.href = "/";
  };

  const handleSearch = async () => {
    setLoading(true);
    try {
      let response;
      if (toolId) {
        // Buscar historial de herramienta
        response = await axios.get(`${API_URL}/history/${toolId}`);
      } else if (initDate && endDate) {
        // Buscar por rango de fechas
        response = await axios.get(`${API_URL}/search`, {
          params: { init: initDate, end: endDate },
        });
      } else {
        alert("Debes ingresar un ID de herramienta o un rango de fechas");
        setLoading(false);
        return;
      }
      setResults(response.data);
    } catch (error) {
      console.error("Error buscando Kardex", error);
      alert("Error al buscar en el Kardex");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      <h1
        style={{
          fontSize: "2.5rem",
          display: "inline-block",
          padding: "5px 10px",
          marginBottom: "20px",
        }}
      >
        Kardex
      </h1>

      {/* Filtros */}
      <div style={{ marginBottom: "20px" }}>
        <input
          type="date"
          value={initDate}
          onChange={(e) => setInitDate(e.target.value)}
          style={{
            padding: "10px",
            borderRadius: "8px",
            border: "1px solid #ccc",
            marginRight: "10px",
          }}
        />

        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          style={{
            padding: "10px",
            borderRadius: "8px",
            border: "1px solid #ccc",
            marginRight: "10px",
          }}
        />

        <input
          type="text"
          placeholder="ID de herramienta"
          value={toolId}
          onChange={(e) => setToolId(e.target.value)}
          style={{
            padding: "10px",
            borderRadius: "8px",
            border: "1px solid #ccc",
            marginRight: "10px",
          }}
        />

        <button
          onClick={handleSearch}
          style={{
            padding: "10px 20px",
            borderRadius: "30px",
            backgroundColor: "#1800ad",
            color: "white",
            border: "none",
            cursor: "pointer",
          }}
        >
          Buscar
        </button>
      </div>

      {/* Tabla de resultados */}
      <table style={{ borderCollapse: "collapse", width: "80%" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid black", padding: "8px" }}>Fecha</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Movimiento</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Cantidad</th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr>
              <td colSpan={3} style={{ textAlign: "center", padding: "8px" }}>
                Cargando...
              </td>
            </tr>
          ) : results.length === 0 ? (
            <tr>
              <td colSpan={3} style={{ textAlign: "center", padding: "8px" }}>
                No hay datos
              </td>
            </tr>
          ) : (
            results.map((item) => (
              <tr key={item.id}>
                <td style={{ border: "1px solid black", padding: "8px" }}>
                  {item.date}
                </td>
                <td style={{ border: "1px solid black", padding: "8px" }}>
                  {item.description}
                </td>
                <td style={{ border: "1px solid black", padding: "8px" }}>
                  {item.quantity}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}


