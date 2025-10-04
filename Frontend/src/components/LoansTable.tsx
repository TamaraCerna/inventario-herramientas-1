import React, { useEffect, useState } from "react";
import { getActiveLoans } from "../service/reportService";

export const LoansTable: React.FC = () => {
  const [loans, setLoans] = useState<any[]>([]);

  useEffect(() => {
    getActiveLoans()
      .then(setLoans)
      .catch(console.error);
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h2 style={{ marginBottom: "20px" }}> Préstamos Activos</h2>
      <table
        style={{
          borderCollapse: "collapse",
          width: "100%",
          boxShadow: "0px 4px 8px rgba(0,0,0,0.1)"
        }}
      >
        <thead style={{ backgroundColor: "#b30000", color: "white" }}>
          <tr>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Cliente</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Herramienta</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Estado</th>
            <th style={{ padding: "10px", border: "1px solid #ddd" }}>Detalle</th>
          </tr>
        </thead>
        <tbody>
          {loans.length === 0 ? (
            <tr>
              <td colSpan={4} style={{ textAlign: "center", padding: "15px" }}>
                No hay préstamos activos
              </td>
            </tr>
          ) : (
            loans.map((loan) => (
              <tr key={loan.loanId}>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>
                  {loan.clientName}
                </td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>
                  {loan.toolName}
                </td>
                <td style={{ border: "1px solid #ddd", padding: "8px" }}>
                  {loan.loanState}
                </td>
                <td
                  style={{
                    border: "1px solid #ddd",
                    padding: "8px",
                    color: loan.estadoDetalle === "Atrasado" ? "red" : "green",
                    fontWeight: "bold"
                  }}
                >
                  {loan.estadoDetalle}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};
export default LoansTable;
