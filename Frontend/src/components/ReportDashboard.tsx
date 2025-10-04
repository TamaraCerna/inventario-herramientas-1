import React, { useEffect, useState } from "react";
import { getActiveLoans, getOverdueCustomers, getRankingTools } from "../service/reportService";

interface Loan {
  id: number;
  startDate: string;
  endDate: string;
  state: string;
  tool: { idTool: number; name: string };
  client: { id: number; name: string };
}

interface Client {
  id: number;
  name: string;
  email: string;
}

export const ReportDashboard: React.FC = () => {
  const [loans, setLoans] = useState<Loan[]>([]);
  const [clients, setClients] = useState<Client[]>([]);
  const [ranking, setRanking] = useState<any[]>([]);

  useEffect(() => {
    // Cargar datos al montar el componente
    getActiveLoans().then(setLoans).catch(console.error);
    getOverdueCustomers().then(setClients).catch(console.error);
    getRankingTools().then(setRanking).catch(console.error);
  }, []);

  return (
    <div style={{ padding: "20px", fontFamily: "Arial" }}>
      <h1>📊 Reportes</h1>

      {/* RF6.1 - Préstamos activos */}
      <h2>Préstamos Activos</h2>
      <table style={{ borderCollapse: "collapse", width: "100%", marginBottom: "20px" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid black", padding: "8px" }}>ID</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Cliente</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Herramienta</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Estado</th>
          </tr>
        </thead>
        <tbody>
          {loans.length === 0 ? (
            <tr>
              <td colSpan={4} style={{ textAlign: "center", padding: "8px" }}>Sin préstamos activos</td>
            </tr>
          ) : (
            loans.map((loan) => (
              <tr key={loan.id}>
                <td style={{ border: "1px solid black", padding: "8px" }}>{loan.id}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{loan.client?.name}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{loan.tool?.name}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{loan.state}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* RF6.2 - Clientes morosos */}
      <h2>Clientes con Préstamos Vencidos</h2>
      <table style={{ borderCollapse: "collapse", width: "100%", marginBottom: "20px" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid black", padding: "8px" }}>ID</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Nombre</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Email</th>
          </tr>
        </thead>
        <tbody>
          {clients.length === 0 ? (
            <tr>
              <td colSpan={3} style={{ textAlign: "center", padding: "8px" }}>Sin clientes morosos</td>
            </tr>
          ) : (
            clients.map((c) => (
              <tr key={c.id}>
                <td style={{ border: "1px solid black", padding: "8px" }}>{c.id}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{c.name}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{c.email}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* RF6.3 - Ranking herramientas */}
      <h2>Ranking de Herramientas</h2>
      <table style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr>
            <th style={{ border: "1px solid black", padding: "8px" }}>Herramienta</th>
            <th style={{ border: "1px solid black", padding: "8px" }}>Cantidad de Préstamos</th>
          </tr>
        </thead>
        <tbody>
          {ranking.length === 0 ? (
            <tr>
              <td colSpan={2} style={{ textAlign: "center", padding: "8px" }}>Sin datos de ranking</td>
            </tr>
          ) : (
            ranking.map((r, index) => (
              <tr key={index}>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r[0]}</td>
                <td style={{ border: "1px solid black", padding: "8px" }}>{r[1]}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};
