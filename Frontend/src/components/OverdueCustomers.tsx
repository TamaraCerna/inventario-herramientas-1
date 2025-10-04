import React, { useEffect, useState } from "react";
import { getOverdueCustomers } from "../service/reportService";

interface Client {
  clientId: number;
  clientName: string;
  clientEmail: string;
  clientRut: string;
  clientPhone: number;
  clientState: string;
}

export const OverdueCustomers: React.FC = () => {
  const [clients, setClients] = useState<Client[]>([]);

  useEffect(() => {
    getOverdueCustomers()
      .then(setClients)
      .catch((err) => console.error("Error cargando clientes:", err));
  }, []);

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      <h2 style={{ marginBottom: "20px" }}> Clientes con Atraso</h2>
      <table
        style={{
          borderCollapse: "collapse",
          width: "100%",
          boxShadow: "0px 4px 8px rgba(0,0,0,0.1)",
        }}
      >
        <thead style={{ backgroundColor: "#b30000", color: "white" }}>
          <tr>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>ID</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Nombre</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>RUT</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Teléfono</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Email</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Estado</th>
          </tr>
        </thead>
        <tbody>
          {clients.length === 0 ? (
            <tr>
              <td colSpan={6} style={{ textAlign: "center", padding: "15px" }}>
                No hay clientes con atraso
              </td>
            </tr>
          ) : (
            clients.map((c) => (
              <tr key={c.clientId}>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientId}</td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientName}</td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientRut}</td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientPhone}</td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientEmail}</td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>{c.clientState}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default OverdueCustomers;

